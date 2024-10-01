package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.ReplenishCardRequest;
import com.example.lucky__bank.dto.ReplenishmentDto;
import com.example.lucky__bank.service.CardService;
import com.example.lucky__bank.service.ReplenishmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/replenishments")
@RequiredArgsConstructor
public class ReplenishmentController {

    private final ReplenishmentService replenishmentService;



    @PostMapping("/replenish")
    public ResponseEntity<String> replenishCard(@RequestBody ReplenishCardRequest request) {

        replenishmentService.replenishCard(request.getCardNumber(), request.getAmount());
        return ResponseEntity.ok("Карта пополнена на " + request.getCardNumber() + " сумму " + request.getAmount());
    }


    @GetMapping("/history/{cardId}")
    public ResponseEntity<List<ReplenishmentDto>> getReplenishmentHistory(@PathVariable Long cardId) {
        List<ReplenishmentDto> replenishments = replenishmentService.getReplenishmentsByCard(cardId);
        return ResponseEntity.ok(replenishments);
    }
}
