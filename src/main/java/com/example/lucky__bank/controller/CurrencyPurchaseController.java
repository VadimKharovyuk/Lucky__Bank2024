package com.example.lucky__bank.controller;

import com.example.lucky__bank.Exception.InsufficientFundsException;
import com.example.lucky__bank.Request.CurrencyPurchaseRequest;
import com.example.lucky__bank.dto.CardDTO;

import com.example.lucky__bank.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
@Slf4j
public class CurrencyPurchaseController {

    private final CardService cardService;


    @PostMapping("/buy")
    public ResponseEntity<?> buyCurrency(@RequestBody CurrencyPurchaseRequest request) {
        log.info("Запрос на покупку валюты: {}", request);
        try {
            CardDTO updatedCard = cardService.buyCurrency(
                    request.getUserId(),
                    request.getCardId(),
                    request.getAmount(),
                    request.getFromCurrency(),
                    request.getToCurrency()
            );
            return ResponseEntity.ok(updatedCard);
        } catch (InsufficientFundsException e) {
            log.error("Недостаточно средств: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Недостаточно средств: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Неверный запрос: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Неверный запрос: " + e.getMessage());
        } catch (Exception e) {
            log.error("Произошла ошибка: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка: " + e.getMessage());
        }
    }
}