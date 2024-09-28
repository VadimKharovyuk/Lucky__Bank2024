package com.example.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class DepositDto {
    private Long id;

    private Long userId;
    private Long cardId;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
