package com.example.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReplenishmentDto {
    public Long id;

    private String cardNumber;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
