package com.expense.tracking.backend.expensetrackingbackend.repository;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.expense.tracking.backend.expensetrackingbackend.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query("SELECT DISTINCT e.msgTimestamp FROM Expense e ORDER BY e.msgTimestamp DESC")
    List<LocalDateTime> findDistinctMsgTimestamps();

    @Query("SELECT e FROM Expense e WHERE e.msgTimestamp = (SELECT MAX(e2.msgTimestamp) FROM Expense e2)")
    List<Expense> findLatestMessageExpenses();

    List<Expense> findByMsgTimestamp(LocalDateTime msgTimestamp);
    
    List<Expense> findByMsgTimestampBetween(LocalDateTime start, LocalDateTime end);
}
