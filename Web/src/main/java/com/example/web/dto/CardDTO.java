package com.example.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
    private Long id;

    private String cardNumber;
    private String cardType;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;
    private String cvv;

    public enum CardType {
        DEBIT,
        CREDIT,
        PREPAID
    }
}
