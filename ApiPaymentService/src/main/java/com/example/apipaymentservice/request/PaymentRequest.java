package com.example.apipaymentservice.request;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;


@Data
public class PaymentRequest {
    private String cardNumber;
    private Date expirationDate;
    private String cvv;
    private BigDecimal amount;
}
