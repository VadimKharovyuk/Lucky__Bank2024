package com.example.lucky__bank.controller;

import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.maper.CardMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Slf4j
public class CardController {


    private final CardService cardService;

    //удаление карты
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        log.info("карта пользователя удалена с id "+ id);
        cardService.deleteCardById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CardDTO>> getCardsByUserId(@PathVariable Long userId) {
        List<CardDTO> cards = cardService.getCardsByUserId(userId);
        return ResponseEntity.ok(cards);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> findById(@PathVariable Long id) {
        CardDTO cardDTO = cardService.findById(id);
        return ResponseEntity.ok(cardDTO);
    }

    @PostMapping
    public ResponseEntity<CardDTO> createCard(
            @RequestParam Long userId,
            @RequestParam String cardType) {

        CardDTO cardDTO = cardService.createCard(userId, cardType);
        return ResponseEntity.ok(cardDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CardDTO>> getAllCards() {
        List<CardDTO> cards = cardService.getAllCard(); // Убедитесь, что метод в сервисе назван корректно
        return ResponseEntity.ok(cards); // Возвращаем 200 OK с списком карт
    }
}

