package com.expense.tracking.backend.expensetrackingbackend.repository;
import com.expense.tracking.backend.expensetrackingbackend.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByMessageId(Long messageId);
}
