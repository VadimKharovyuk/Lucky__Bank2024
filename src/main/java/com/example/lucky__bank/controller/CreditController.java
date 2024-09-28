package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.CreditRequestDto;
import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.service.CreditCreationService;
import com.example.lucky__bank.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditCreationService creditCreationService;
    private final CreditService creditService;



    //создание кредита
    @PostMapping
    public ResponseEntity<CreditDto> createCredit(@RequestBody CreditRequestDto requestDto) {
        CreditDto createdCredit = creditCreationService.createCredit(
                requestDto.getUserId(),
                requestDto.getLoanAmount(),
                requestDto.getInterestRate(),
                requestDto.getTermInMonths(),
                requestDto.getPurpose()
        );
        return ResponseEntity.ok(createdCredit);
    }

    // Метод для получения всех кредитов
    @GetMapping
    public ResponseEntity<List<CreditDto>> getAllCredits() {
        List<CreditDto> credits = creditService.getAllCredits();
        return ResponseEntity.ok(credits);
    }

    // Метод для одобрения кредита
    @PutMapping("/{creditId}/approve")
    public ResponseEntity<CreditDto> approveCredit(@PathVariable Long creditId) {
        CreditDto approvedCredit = creditService.approveCredit(creditId);
        return ResponseEntity.ok(approvedCredit);
    }
}