package com.expense.tracking.backend.expensetrackingbackend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDto {
    private Long id;
    private LocalDateTime timestamp;
    private List<ExpenseDto> expenses;

    public MessageDto() {
        this.id = null;
        this.expenses = null;
        this.timestamp = null;
    }

    public MessageDto(Long id, LocalDateTime timestamp, List<ExpenseDto> expenses) {
        this.id = id;
        this.timestamp = timestamp;
        this.expenses = expenses;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<ExpenseDto> getExpenses() {
        return expenses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setExpenses(List<ExpenseDto> expenses) {
        this.expenses = expenses;
    }
}

