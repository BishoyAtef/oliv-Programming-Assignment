package com.expense.tracking.backend.expensetrackingbackend.controller;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expense.tracking.backend.expensetrackingbackend.model.Expense;
import com.expense.tracking.backend.expensetrackingbackend.repository.ExpenseRepository;
import com.expense.tracking.backend.expensetrackingbackend.service.ExpenseParserService;

@RestController
@RequestMapping("api/v1/expense-tree")
public class ExpenseTreeHandler {
    private final ExpenseRepository expenseRepository;
    private final ExpenseParserService expenseParserService;

    public ExpenseTreeHandler(ExpenseRepository expenseRepository, ExpenseParserService expenseParserService) {
        this.expenseRepository = expenseRepository;
        this.expenseParserService = expenseParserService;
    }

    @GetMapping("/")
    public List<Map<String, Object>> getExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        // Group expenses by msgTimestamp
        Map<LocalDateTime, List<Expense>> grouped = expenses.stream().collect(Collectors.groupingBy(Expense::getMsgTimestamp));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<LocalDateTime, List<Expense>> entry : grouped.entrySet()) {
            String[] lines = entry.getValue().stream().map(exp -> exp.getExpense()).toArray(String[]::new);
            Map<String, Object> tree = this.expenseParserService.parseToJsonTree(lines);
            tree.put("timestamp", entry.getKey().toString());
            result.add(tree);
        }
        return result;
    }

    @GetMapping("/msgs_timestamps")
    public List<LocalDateTime> getAllTimestamps() { // added for convenience 
        return expenseRepository.findDistinctMsgTimestamps();
    }

    @GetMapping("/by-timestamp")
    public Map<String, Object> getByTimestamp(@RequestParam("value") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime value) {
        List<Expense> expenses = expenseRepository.findByMsgTimestamp(value);
        String[] lines = expenses.stream().map(Expense::getExpense).toArray(String[]::new);
        return this.expenseParserService.parseToJsonTree(lines);
    }

    @GetMapping("/latest")
    public Map<String, Object> getLatestMessageExpensesAsTree() {
        List<Expense> expenses = expenseRepository.findLatestMessageExpenses();
        String[] lines = expenses.stream().map(Expense::getExpense).toArray(String[]::new);
        return this.expenseParserService.parseToJsonTree(lines);
    }

    @GetMapping("/by-date-range")
    public Map<String, Object> getExpensesByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Expense> expenses = expenseRepository.findByMsgTimestampBetween(start, end);
        String[] lines = expenses.stream().map(Expense::getExpense).toArray(String[]::new);
        return this.expenseParserService.parseToJsonTree(lines);
    }
}
