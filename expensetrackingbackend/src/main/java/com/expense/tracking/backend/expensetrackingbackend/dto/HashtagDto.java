package com.expense.tracking.backend.expensetrackingbackend.dto;

public class HashtagDto {
    private Long id;
    private String name;

    public HashtagDto() {
        this.id = null;
        this.name = null;
    }

    public HashtagDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
