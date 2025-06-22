package com.expense.tracking.backend.expensetrackingbackend.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import com.expense.tracking.backend.expensetrackingbackend.model.Expense;
import com.expense.tracking.backend.expensetrackingbackend.model.Hashtag;
import com.expense.tracking.backend.expensetrackingbackend.dto.ExpenseTreeDto;

@Service
public class ExpenseParserService {

    class TreeNode {
        String tag;
        int amount;
        int sum;
        Map<String, TreeNode> children = new LinkedHashMap<>();

        TreeNode(String tag) {
            this.tag = tag;
        }

        void addChild(TreeNode child) {
            children.put(child.tag, child);
        }

        void addExpense(List<String> tags, int amount) {
            TreeNode current = this;
            for (String singleTag : tags) {
                current = current.children.computeIfAbsent(singleTag, TreeNode::new);
            }
            current.amount += amount;
        }

        void computeSum() {
            sum = amount;
            for (TreeNode child : children.values()) {
                child.computeSum();
                sum += child.sum;
            }
        }

        ExpenseTreeDto toDto() {
            List<ExpenseTreeDto> childDtos = children.values().stream()
                .map(TreeNode::toDto)
                .toList();

            return new ExpenseTreeDto(tag, amount, sum, childDtos.isEmpty() ? null : childDtos);
        }
    }

    class ExpenseEntry {
        List<String> tags;
        int amount;

        ExpenseEntry(List<String> tags, int amount) {
            this.tags = tags;
            this.amount = amount;
        }
    }

    public ExpenseTreeDto parseToTreeDto(List<Expense> expenses) {
        TreeNode root = new TreeNode("#total");
        List<ExpenseEntry> entries = new ArrayList<>();
        int untaggedTotal = 0;

        for (Expense expense : expenses) {
            int amount = expense.getAmount();
            List<String> tags = expense.getHashtags().stream().map(Hashtag::getName).toList();
            if (!tags.isEmpty()) {
                entries.add(new ExpenseEntry(tags, amount));
            } else {
                untaggedTotal += amount;
            }
        }

        while (!entries.isEmpty()) {
            Map<String, Integer> tagSums = new LinkedHashMap<>();
            Map<String, Integer> tagFirstSeenIndex = new HashMap<>();

            for (int i = 0; i < entries.size(); i++) {
                ExpenseEntry entry = entries.get(i);
                for (int j = 0; j < entry.tags.size(); j++) {
                    String tag = entry.tags.get(j);
                    tagSums.put(tag, tagSums.getOrDefault(tag, 0) + entry.amount);
                    tagFirstSeenIndex.putIfAbsent(tag, i * 100 + j);
                }
            }

            List<String> sortedTags = new ArrayList<>(tagSums.keySet());
            sortedTags.sort((a, b) -> {
                int cmp = Integer.compare(tagSums.get(b), tagSums.get(a));
                if (cmp == 0) {
                    return Integer.compare(tagFirstSeenIndex.get(a), tagFirstSeenIndex.get(b));
                }
                return cmp;
            });

            if (sortedTags.isEmpty()) break;

            String selectedTag = sortedTags.get(0);
            List<ExpenseEntry> matched = new ArrayList<>();
            List<ExpenseEntry> remaining = new ArrayList<>();

            for (ExpenseEntry entry : entries) {
                if (entry.tags.contains(selectedTag)) {
                    List<String> newTags = new ArrayList<>(entry.tags);
                    newTags.remove(selectedTag);
                    matched.add(new ExpenseEntry(newTags, entry.amount));
                } else {
                    remaining.add(entry);
                }
            }

            TreeNode child = new TreeNode(selectedTag);
            for (ExpenseEntry e : matched) {
                if (!e.tags.isEmpty()) {
                    child.addExpense(e.tags, e.amount);
                } else {
                    child.amount += e.amount;
                }
            }

            root.addChild(child);
            entries = remaining;
        }

        root.amount += untaggedTotal;
        root.computeSum();
        return root.toDto();
    }

    public int parseAmount(String line) {
        Matcher matcher = Pattern.compile("^\\s*([\\d,]+)").matcher(line);
        if (matcher.find()) {
            String number = matcher.group(1).replace(",", "");
            return Integer.parseInt(number);
        }
        return 0;
    }

    public List<String> extractHashtags(String line) {
        List<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("#\\w+").matcher(line);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
}
