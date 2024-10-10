package com.example.lucky__bank.dto;

import com.example.lucky__bank.model.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {
    private Long id;

    private String cardNumber;
    private String cardType;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;
    private String cvv;
    private LocalDate lastBonusDate ;
}