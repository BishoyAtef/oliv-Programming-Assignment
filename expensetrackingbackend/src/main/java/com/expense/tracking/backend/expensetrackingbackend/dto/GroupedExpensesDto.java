package com.expense.tracking.backend.expensetrackingbackend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.expense.tracking.backend.expensetrackingbackend.model.Expense;

public class GroupedExpensesDto {
    private LocalDateTime timestamp;
    private List<Expense> expenses;

    public GroupedExpensesDto() {}

    public GroupedExpensesDto(LocalDateTime timestamp, List<Expense> expenses) {
        this.timestamp = timestamp;
        this.expenses = expenses;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}
