package com.example.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;

    private String fromCardNumber;
    private String toCardNumber;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String description;
}
