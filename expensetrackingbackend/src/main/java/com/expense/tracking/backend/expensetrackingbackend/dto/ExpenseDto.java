package com.expense.tracking.backend.expensetrackingbackend.dto;
import java.time.LocalDateTime;
import java.util.List;

public class ExpenseDto {
    private int amount;
    private String description;
    private List<String> tags;
    private LocalDateTime timestamp;

    public ExpenseDto() {}

    public ExpenseDto(int amount, String description, List<String> tags, LocalDateTime timestamp) {
        this.amount = amount;
        this.description = description;
        this.tags = tags;
        this.timestamp = timestamp;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
