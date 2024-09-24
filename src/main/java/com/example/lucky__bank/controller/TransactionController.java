package com.example.lucky__bank.controller;

import com.example.lucky__bank.dto.TransactionDto;
import com.example.lucky__bank.service.MoneyTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
public class TransactionController {
    private final MoneyTransferService moneyTransferService;

    @PostMapping
    public ResponseEntity<TransactionDto> createTranfer(
            @RequestParam String fromCardNumber,
            @RequestParam String toCardNumber,
            @RequestParam String description,
            @RequestParam BigDecimal amount
            ){
       TransactionDto transactionDto = moneyTransferService.transferMoney(fromCardNumber,toCardNumber,amount,description);
        return ResponseEntity.ok(transactionDto);
    }
}
