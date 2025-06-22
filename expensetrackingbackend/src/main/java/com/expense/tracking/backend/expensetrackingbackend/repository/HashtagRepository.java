package com.expense.tracking.backend.expensetrackingbackend.repository;

import com.expense.tracking.backend.expensetrackingbackend.model.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);
}
