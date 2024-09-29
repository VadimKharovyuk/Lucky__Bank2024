package com.example.lucky__bank.dto;

import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditDto {

    private Long id;
    private Long userId;

    private Long cardId;
    private BigDecimal amount;
    private Integer termInMonths;
    private String purpose;
    private BigDecimal monthlyPayment;
    private double interestRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PaymentScheduleDto> paymentSchedules;
}