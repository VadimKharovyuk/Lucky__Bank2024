package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Deposit;
import com.example.lucky__bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit,Long> {
    Optional<Deposit> findByUserAndCard(User user, Card card);
}
