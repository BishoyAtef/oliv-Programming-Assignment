package com.expense.tracking.backend.expensetrackingbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ExpenseRequestDto {
    
    @NotBlank(message = "Expense must not be empty")
    @Pattern(
        regexp = "^(?!0+(?:[.,]0+)?$)(?=0*[1-9]|0*\\d*[.,]\\d*[1-9])\\d+(?:[.,]\\d+)?\\s+.+$",
        message = "Expense must start with an amount followed by a description"
    )
    private String expense;

    public ExpenseRequestDto() {
    }

    public ExpenseRequestDto(String expense) {
        this.expense = expense;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }
}
