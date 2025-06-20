package com.expense.tracking.backend.expensetrackingbackend;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1/expenses")
public class ExpenseHandler {
    private final ExpenseRepository expenseRepository;

    public ExpenseHandler(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/")
    public List<Expense> addExpenses(@RequestBody List<Expense> expenses) {
        LocalDateTime msgTimestamp = LocalDateTime.now();
        expenses.forEach(expense -> {
            if (expense.getMsgTimestamp() == null) {
                expense.setMsgTimestamp(msgTimestamp);
            }
        });
        return expenseRepository.saveAll(expenses);
    }

    @GetMapping("/")
    public Map<LocalDateTime, List<Expense>> getExpenses() {
        // return expenseRepository.findAll();
        List<Expense> expenses = expenseRepository.findAll();
        // Group expenses by msgTimestamp
        Map<LocalDateTime, List<Expense>> grouped = expenses.stream().collect(Collectors.groupingBy(Expense::getMsgTimestamp));
        return grouped;
    }

    @GetMapping("/msgs_timestamps")
    public List<LocalDateTime> getAllTimestamps() {
        return expenseRepository.findDistinctMsgTimestamps();
    }

    @GetMapping("/latest")
    public List<Expense> getLatestMessageExpenses() {
        return expenseRepository.findLatestMessageExpenses();
    }

    @GetMapping("/by-timestamp")
    public List<Expense> getByTimestamp(@RequestParam("value") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime value) {
        return expenseRepository.findByMsgTimestamp(value);
    }
}
