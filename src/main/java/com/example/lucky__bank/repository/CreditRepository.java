package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit,Long> {
}
