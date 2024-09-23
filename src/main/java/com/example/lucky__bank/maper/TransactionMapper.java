package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.TransactionDto;
import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.model.Transaction;
import com.example.lucky__bank.model.User;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {


    public TransactionDto toDTO(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .toCardNumber(transaction.getToCardNumber())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .fromCardNumber(transaction.getFromCardNumber())
                .timestamp(transaction.getTimestamp())
                .build();
    }


    public Transaction convertToEntity(TransactionDto dto) {
        Transaction transaction = new Transaction();
        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setTimestamp(dto.getTimestamp());
        transaction.setToCardNumber(dto.getToCardNumber());
        transaction.setFromCardNumber(dto.getFromCardNumber());

        return transaction;
    }
}
