package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.ReplenishmentDto;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Replenishment;
import org.springframework.stereotype.Component;

@Component
public class ReplenishmentMapper {
    public ReplenishmentDto toDto(Replenishment replenishment) {
        return ReplenishmentDto.builder()
                .id(replenishment.getId())
                .cardNumber(String.valueOf(replenishment.getCard().getId()))
                .amount(replenishment.getAmount())
                .createdAt(replenishment.getCreatedAt())
                .build();
    }


    public Replenishment toEntity(ReplenishmentDto replenishmentDto, Card card) {
        Replenishment replenishment = new Replenishment();
        replenishment.setId(replenishmentDto.getId());
        replenishment.setCard(card);
        replenishment.setAmount(replenishmentDto.getAmount());
        replenishment.setCreatedAt(replenishmentDto.getCreatedAt());
        return replenishment;
    }

}
