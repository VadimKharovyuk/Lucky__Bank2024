package com.example.lucky__bank.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDto {

    private Long id;

    private String fromCardNumber;
    private String toCardNumber;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String description;


}
