package com.example.lucky__bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String fromCardNumber; // Номер карты отправителя
    private String toCardNumber;   // Номер карты получателя
    private BigDecimal amount;         // Сумма перевода
    private LocalDateTime timestamp; // Время транзакции
    private String description;  // Описание транзакции


}
