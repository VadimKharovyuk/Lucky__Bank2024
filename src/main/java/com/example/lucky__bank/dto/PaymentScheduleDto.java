package com.example.lucky__bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentScheduleDto {
    private Long id;
    private Long creditId;
    private BigDecimal paymentAmount;
    private LocalDate paymentDate;
    private boolean paid;

    private BigDecimal interestAmount;
    private BigDecimal principalAmount;
}
