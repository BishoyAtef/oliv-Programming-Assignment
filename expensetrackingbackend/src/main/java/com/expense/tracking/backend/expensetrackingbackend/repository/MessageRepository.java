package com.expense.tracking.backend.expensetrackingbackend.repository;

import com.expense.tracking.backend.expensetrackingbackend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT DISTINCT m.timestamp FROM Message m ORDER BY m.timestamp DESC")
    List<LocalDateTime> findDistinctTimestamps();

    @Query("SELECT m FROM Message m LEFT JOIN FETCH m.expenses e LEFT JOIN FETCH e.hashtags")
    List<Message> findAllWithExpensesAndHashtags();

    Optional<Message> findByTimestamp(LocalDateTime timestamp);

    @Query("SELECT m FROM Message m WHERE m.timestamp = (SELECT MAX(m2.timestamp) FROM Message m2)")
    Optional<Message> findLatestMessage();

    List<Message> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
