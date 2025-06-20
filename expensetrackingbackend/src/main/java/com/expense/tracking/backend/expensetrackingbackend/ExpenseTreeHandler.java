package com.expense.tracking.backend.expensetrackingbackend;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1/expenses/tree")
public class ExpenseTreeHandler {
    private final ExpenseRepository expenseRepository;

    public ExpenseTreeHandler(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/")
    public List<Map<String, Object>> getExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        // Group expenses by msgTimestamp
        Map<LocalDateTime, List<Expense>> grouped = expenses.stream().collect(Collectors.groupingBy(Expense::getMsgTimestamp));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<LocalDateTime, List<Expense>> entry : grouped.entrySet()) {
            String[] lines = entry.getValue().stream().map(exp -> exp.getExpense()).toArray(String[]::new);
            Map<String, Object> tree = ExpenseParser.parseToJsonTree(lines);
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
        return ExpenseParser.parseToJsonTree(lines);
    }

    @GetMapping("/latest-tree")
    public Map<String, Object> getLatestMessageExpensesAsTree() {
        List<Expense> expenses = expenseRepository.findLatestMessageExpenses();
        String[] lines = expenses.stream().map(Expense::getExpense).toArray(String[]::new);
        return ExpenseParser.parseToJsonTree(lines);
    }
}
