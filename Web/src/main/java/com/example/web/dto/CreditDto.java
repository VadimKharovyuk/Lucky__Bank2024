package com.example.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
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
