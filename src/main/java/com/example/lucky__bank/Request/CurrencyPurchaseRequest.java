package com.example.lucky__bank.Request;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class CurrencyPurchaseRequest {
    private Long userId;
    private Long cardId;
    private BigDecimal amount;
    private String fromCurrency;
    private String toCurrency;
}
