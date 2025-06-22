package com.expense.tracking.backend.expensetrackingbackend.dto;

import java.util.List;

public class ExpenseTreeDto {
    private String tag;
    private int amount;
    private int sum;
    private List<ExpenseTreeDto> children;

    public ExpenseTreeDto() {
        tag = null;
        amount = -1;
        sum = -1;
        children = null;
    }

    public ExpenseTreeDto(String tag, int amount, int sum, List<ExpenseTreeDto> children) {
        this.tag = tag;
        this.amount = amount;
        this.sum = sum;
        this.children = children;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public List<ExpenseTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<ExpenseTreeDto> children) {
        this.children = children;
    }
}

