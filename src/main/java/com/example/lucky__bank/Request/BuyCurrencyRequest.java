package com.example.lucky__bank.Request;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class BuyCurrencyRequest {
    private Long sourceCardId;
    private Long targetCardId;
    private BigDecimal amount;
}
