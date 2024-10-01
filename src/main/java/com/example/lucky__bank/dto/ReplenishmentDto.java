package com.example.lucky__bank.dto;

import com.example.lucky__bank.model.Card;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ReplenishmentDto {

    public Long id;

    private String cardNumber;
    private BigDecimal amount;
    private LocalDateTime createdAt;


}
