package com.example.lucky__bank.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyTransferRequest {
    private String fromCardNumber;
    private String toCardNumber;
    private BigDecimal amount;
    private String description;
}
