package com.example.lucky__bank.controller;

import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.maper.CardMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<CardDTO> createCard(
            @RequestParam Long userId,
            @RequestParam String cardType) {

        CardDTO cardDTO = cardService.createCard(userId, cardType);
        return ResponseEntity.ok(cardDTO);
    }
}