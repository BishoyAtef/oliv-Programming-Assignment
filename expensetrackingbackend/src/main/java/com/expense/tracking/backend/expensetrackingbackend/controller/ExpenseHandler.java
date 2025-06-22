package com.expense.tracking.backend.expensetrackingbackend.controller;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expense.tracking.backend.expensetrackingbackend.dto.ExpenseDto;
import com.expense.tracking.backend.expensetrackingbackend.dto.ExpenseRequestDto;
import com.expense.tracking.backend.expensetrackingbackend.dto.GroupedExpensesDto;
import com.expense.tracking.backend.expensetrackingbackend.model.Expense;
import com.expense.tracking.backend.expensetrackingbackend.repository.ExpenseRepository;
import com.expense.tracking.backend.expensetrackingbackend.service.ExpenseParserService;

@RestController
@RequestMapping("api/v1/expense")
public class ExpenseHandler {
    private final ExpenseRepository expenseRepository;
    private final ExpenseParserService expenseParserService;

    public ExpenseHandler(ExpenseRepository expenseRepository, ExpenseParserService expenseParserService) {
        this.expenseRepository = expenseRepository;
        this.expenseParserService = expenseParserService;
    }

    @PostMapping("/")
    public List<Expense> addExpenses(@RequestBody List<ExpenseRequestDto> requests) {
        LocalDateTime msgTimestamp = LocalDateTime.now();
        List<Expense> expenses = requests.stream()
            .map(req -> {
                Expense expense = new Expense(req.getExpense());
                expense.setMsgTimestamp(msgTimestamp);
                return expense;
            })
            .toList();
        return expenseRepository.saveAll(expenses);
    }

    @GetMapping("/")
    public List<GroupedExpensesDto> getExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream()
            .collect(Collectors.groupingBy(Expense::getMsgTimestamp))
            .entrySet()
            .stream()
            .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey())) // descending order
            .map(entry -> new GroupedExpensesDto(entry.getKey(), entry.getValue()))
            .toList();
    }

    @GetMapping("/msgs_timestamps")
    public List<LocalDateTime> getAllTimestamps() {
        return expenseRepository.findDistinctMsgTimestamps();
    }

    @GetMapping("/latest")
    public List<ExpenseDto> getLatestMessageExpenses() {
        List<Expense> latestExpenses = expenseRepository.findLatestMessageExpenses();
        return latestExpenses.stream()
            .map(exp -> {
                String expenseStr = exp.getExpense();
                int amount = this.expenseParserService.parseAmount(expenseStr);
                List<String> tags = this.expenseParserService.extractHashtags(expenseStr);
                return new ExpenseDto(amount, expenseStr, tags, exp.getMsgTimestamp());
            })
            .toList();
    }

    @GetMapping("/by-timestamp")
    public List<ExpenseDto> getByTimestamp(
            @RequestParam("value") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime value) {
        
        List<Expense> expenses = expenseRepository.findByMsgTimestamp(value);
        return expenses.stream()
            .map(exp -> {
                String expenseStr = exp.getExpense();
                int amount = this.expenseParserService.parseAmount(expenseStr);
                List<String> tags = this.expenseParserService.extractHashtags(expenseStr);
                return new ExpenseDto(amount, expenseStr, tags, exp.getMsgTimestamp());
            })
            .toList();
    }

    @GetMapping("/by-date-range")
    public List<ExpenseDto> getExpensesByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<Expense> expenses = expenseRepository.findByMsgTimestampBetween(start, end);
        return expenses.stream()
            .map(exp -> {
                String expenseStr = exp.getExpense();
                int amount = this.expenseParserService.parseAmount(expenseStr);
                List<String> tags = this.expenseParserService.extractHashtags(expenseStr);
                return new ExpenseDto(amount, expenseStr, tags, exp.getMsgTimestamp());
            })
            .toList();
    }   
}
