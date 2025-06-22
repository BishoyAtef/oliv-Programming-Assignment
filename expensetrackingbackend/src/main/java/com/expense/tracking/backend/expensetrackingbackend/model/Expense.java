package com.expense.tracking.backend.expensetrackingbackend.model;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "expense")
    private String expense;

    @Column(name = "msg_timestamp")
    private LocalDateTime msgTimestamp;

    public Expense() {
    }

    public Expense(String expense) {
        this.expense = expense;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpense() {
        return this.expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public LocalDateTime getMsgTimestamp() {
        return this.msgTimestamp;
    }

    public void setMsgTimestamp(LocalDateTime msgTimestamp) {
        this.msgTimestamp = msgTimestamp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.expense);
        hash = 59 * hash + Objects.hashCode(this.msgTimestamp);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Expense other = (Expense) obj;
        if(!Objects.equals(this.id, other.id)) {
            return false;
        }
        if(!Objects.equals(this.msgTimestamp, other.msgTimestamp)) {
            return false;
        }
        return Objects.equals(this.expense, other.expense);
    }
}