package com.example.lucky__bank.dto;

import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditDto {

    private Long id;
    private Long userId;  // ID пользователя
    private Long cardId;  // ID карты
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Если нужно передавать информацию о платежах
    private List<PaymentScheduleDto> paymentSchedules;
}