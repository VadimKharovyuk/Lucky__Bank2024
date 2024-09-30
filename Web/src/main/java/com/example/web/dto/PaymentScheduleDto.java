package com.example.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class PaymentScheduleDto {
    private Long id;
    private Long creditId;
    private BigDecimal paymentAmount;  // Сумма платежа
    private LocalDate paymentDate;  // Дата платежа (заменён тип на LocalDate)
    private boolean paid;

    private BigDecimal interestAmount;  // новое поле
    private BigDecimal principalAmount; // новое поле
}
