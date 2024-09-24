package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.MoneyTransferRequest;
import com.example.lucky__bank.dto.TransactionDto;
import com.example.lucky__bank.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
public class TransactionController {
    private final MoneyTransferService moneyTransferService;


    @PostMapping
    public ResponseEntity<TransactionDto> createTransfer(@RequestBody MoneyTransferRequest moneyTransferRequest) {
        TransactionDto transactionDto = moneyTransferService.transferMoney(
                moneyTransferRequest.getFromCardNumber(),
                moneyTransferRequest.getToCardNumber(),
                moneyTransferRequest.getAmount(),
                moneyTransferRequest.getDescription()
        );
        return ResponseEntity.ok(transactionDto);
    }
}
