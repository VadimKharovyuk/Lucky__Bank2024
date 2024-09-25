package com.example.apipaymentservice.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String fromCardNumber;
    private String toCardNumber;
    private BigDecimal amount;
}
