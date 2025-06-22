package com.expense.tracking.backend.expensetrackingbackend.dto;

public class ExpenseRequestDto {
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
