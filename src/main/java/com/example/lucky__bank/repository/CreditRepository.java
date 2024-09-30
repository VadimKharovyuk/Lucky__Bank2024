package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditRepository extends JpaRepository<Credit,Long> {
    List<Credit> findByUserId(Long userId);
    List<Credit> findByUserAndCard(User user, Card card);
}
