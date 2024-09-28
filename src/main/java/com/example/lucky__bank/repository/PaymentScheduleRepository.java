package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule,Long> {
}
