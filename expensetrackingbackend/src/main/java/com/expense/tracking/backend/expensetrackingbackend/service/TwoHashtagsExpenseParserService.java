package com.expense.tracking.backend.expensetrackingbackend.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class TwoHashtagsExpenseParserService {
    class TreeNode {
        String tag;
        int amount;
        Map<String, TreeNode> children = new LinkedHashMap<>();

        TreeNode(String tag) {
            this.tag = tag;
        }

        void addChild(String subTag, int amount) {
            TreeNode child = children.getOrDefault(subTag, new TreeNode(subTag));
            child.amount += amount;
            children.put(subTag, child);
        }

        Map<String, Object> toMap() {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("tag", tag);
            result.put("amount", amount);
            if (!children.isEmpty()) {
                List<Map<String, Object>> childrenList = new ArrayList<>();
                for (TreeNode child : children.values()) {
                    childrenList.add(child.toMap());
                }
                result.put("children", childrenList);
            }
            return result;
        }
    }

    public Map<String, Object> parseToJsonTree(String[] input) {
        int total = 0;
        Map<String, Map<String, Integer>> grouped = new HashMap<>();

        for (String line : input) {
            int amount = parseAmount(line);
            total += amount;

            List<String> tags = extractHashtags(line);
            String primary = "#other";
            String sub = "#other";

            if (tags.size() == 1) {
                primary = tags.get(0);
            } else if (tags.size() >= 2) {
                primary = tags.get(0);
                sub = tags.get(1);
            }

            grouped.computeIfAbsent(primary, k -> new HashMap<>())
                   .merge(sub, amount, Integer::sum);
        }

        Map<String, Map<String, Integer>> collapsed = new LinkedHashMap<>();
        collapsed.put("#other", new HashMap<>());

        for (Map.Entry<String, Map<String, Integer>> entry : grouped.entrySet()) {
            String primary = entry.getKey();
            int primaryTotal = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();

            if (!primary.equals("#other") && primaryTotal < total * 0.05) {
                collapsed.get("#other").merge("#other", primaryTotal, Integer::sum);
            } else {
                collapsed.put(primary, entry.getValue());
            }
        }

        TreeNode root = new TreeNode("Total");
        root.amount = total;

        for (Map.Entry<String, Map<String, Integer>> entry : collapsed.entrySet()) {
            String primary = entry.getKey();
            TreeNode primaryNode = new TreeNode(primary);

            for (Map.Entry<String, Integer> subEntry : entry.getValue().entrySet()) {
                primaryNode.addChild(subEntry.getKey(), subEntry.getValue());
                primaryNode.amount += subEntry.getValue();
            }

            root.children.put(primary, primaryNode);
        }

        return root.toMap();
    }

    int parseAmount(String line) {
        Matcher matcher = Pattern.compile("^\\s*([\\d,]+)").matcher(line);
        if (matcher.find()) {
            String number = matcher.group(1).replace(",", "");
            return Integer.parseInt(number);
        }
        return 0;
    }

    List<String> extractHashtags(String line) {
        List<String> result = new ArrayList<>();
        Matcher matcher = Pattern.compile("#\\w+").matcher(line);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
}

