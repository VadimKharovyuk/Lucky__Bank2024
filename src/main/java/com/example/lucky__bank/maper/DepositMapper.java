package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.DepositDto;
import com.example.lucky__bank.model.Deposit;
import org.springframework.stereotype.Service;

@Service
public class DepositMapper {

    public DepositDto toDto(Deposit deposit) {
        return DepositDto.builder()
                .userId(deposit.getUser().getId())
                .id(deposit.getId())
                .amount(deposit.getAmount())
                .cardId(deposit.getCard().getId())
                .createdAt(deposit.getCreatedAt())
                .updatedAt(deposit.getUpdatedAt())
                .build();
    }

    public Deposit toEntity(DepositDto depositDto) {
        Deposit deposit = new Deposit();
        deposit.setId(depositDto.getId());
        deposit.setAmount(depositDto.getAmount());
        deposit.setCreatedAt(depositDto.getCreatedAt());
        deposit.setUpdatedAt(depositDto.getUpdatedAt());

        return deposit;
    }
}