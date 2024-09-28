package com.example.lucky__bank.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditRequestDto {
    private Long userId;
    private BigDecimal loanAmount;
    private double interestRate;
    private int termInMonths;
    private String purpose;

}
