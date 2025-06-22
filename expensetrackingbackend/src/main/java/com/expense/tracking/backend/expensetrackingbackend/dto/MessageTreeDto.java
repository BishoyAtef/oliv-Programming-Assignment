package com.expense.tracking.backend.expensetrackingbackend.dto;

import java.util.List;

public class MessageTreeDto {
    private String tag;
    private int amount;
    private int sum;
    private List<MessageTreeDto> children;

    public MessageTreeDto() {
        tag = "";
        amount = 0;
        sum = 0;
        children = null;
    }

    public MessageTreeDto(String tag, int amount, int sum, List<MessageTreeDto> children) {
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

    public List<MessageTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<MessageTreeDto> children) {
        this.children = children;
    }
}

