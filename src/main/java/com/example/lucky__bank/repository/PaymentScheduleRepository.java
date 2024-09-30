package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule,Long> {

    List<PaymentSchedule> findByCredit(Credit credit);

}
