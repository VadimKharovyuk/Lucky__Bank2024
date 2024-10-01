package com.example.lucky__bank.controller;

import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.service.BonusService;
import com.example.lucky__bank.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bonus")
@RequiredArgsConstructor
public class BonusController {
    private final BonusService bonusService;
    private final CardService cardService;


    @PostMapping("/claim/{cardId}")
    public ResponseEntity<Map<String, String>> claimDailyBonus(@PathVariable Long cardId) {
        try {
            BigDecimal bonusAmount = bonusService.addDailyBonus(cardId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Bonus claimed successfully");
            response.put("amount", bonusAmount.toString());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/cards/{userId}")
    public ResponseEntity<List<CardDTO>> getUserCards(@PathVariable Long userId) {
        List<CardDTO> cards = cardService.getCardsByUserId(userId);
        return ResponseEntity.ok(cards);
    }

//    @PostMapping("/claim/{cardId}")
//    public ResponseEntity<Map<String, String>> claimDailyBonus(@PathVariable Long cardId) {
//        try {
//            BigDecimal bonusAmount = bonusService.addDailyBonus(cardId);
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "Bonus claimed successfully");
//            response.put("amount", bonusAmount.toString());
//            return ResponseEntity.ok(response);
//        } catch (IllegalStateException e) {
//            Map<String, String> response = new HashMap<>();
//            response.put("error", e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}