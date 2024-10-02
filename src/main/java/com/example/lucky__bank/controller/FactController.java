package com.example.lucky__bank.controller;

import com.example.lucky__bank.dto.FactDto;
import com.example.lucky__bank.model.Fact;
import com.example.lucky__bank.service.FactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facts")
@RequiredArgsConstructor
public class FactController {
    private final FactService factService;

    // Метод для получения случайного факта
    @GetMapping("/random")
    public ResponseEntity<FactDto> getRandomFact(@RequestParam("type") Fact.FactType type) {
        FactDto randomFact = factService.getRandomFact(type);
        return ResponseEntity.ok(randomFact);
    }
//    @GetMapping("/random")
//    public ResponseEntity<FactDto> getRandomFact(@RequestParam("type") Fact.FactType type) {
//        FactDto randomFact = factService.getRandomFact(type);
//        return ResponseEntity.ok(randomFact);
//    }

}