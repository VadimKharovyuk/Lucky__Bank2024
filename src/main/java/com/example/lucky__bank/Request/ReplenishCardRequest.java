package com.example.lucky__bank.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReplenishCardRequest {
    private String cardNumber;
    private BigDecimal amount;

}
