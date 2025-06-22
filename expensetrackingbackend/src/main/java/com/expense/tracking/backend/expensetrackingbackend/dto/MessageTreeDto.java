package com.expense.tracking.backend.expensetrackingbackend.dto;

import java.time.LocalDateTime;

public class MessageTreeDto {
    private Long id;
    private LocalDateTime timestamp;
    private ExpenseTreeDto expenseTreeDto;

    public MessageTreeDto() {
        this.id = null;
        this.expenseTreeDto = null;
        this.timestamp = null;
    }

    public MessageTreeDto(long id, LocalDateTime timestamp, ExpenseTreeDto expenseTreeDto) {
        this.id = id;
        this.timestamp = timestamp;
        this.expenseTreeDto = expenseTreeDto;
    }

    public Long getId() {
        return this.id;
    }

    public ExpenseTreeDto getExpenseTreeDto() {
        return this.expenseTreeDto;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setExpenseTreeDto(ExpenseTreeDto expenseTreeDto) {
        this.expenseTreeDto = expenseTreeDto;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

