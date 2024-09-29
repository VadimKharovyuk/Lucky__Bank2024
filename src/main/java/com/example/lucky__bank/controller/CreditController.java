package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.CreditRequestDto;
import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.service.CreditCreationService;
import com.example.lucky__bank.service.CreditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditController {

    private final CreditCreationService creditCreationService;
    private final CreditService creditService;


    // Создание кредита
    @PostMapping
    public ResponseEntity<CreditDto> createCredit(@RequestBody @Valid CreditRequestDto requestDto) {
        CreditDto createdCredit = creditCreationService.createCredit(
                requestDto.getUserId(),
                requestDto.getCardId(),  // Теперь передаем cardId
                requestDto.getLoanAmount(),
                requestDto.getInterestRate(),
                requestDto.getTermInMonths(),
                requestDto.getPurpose()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCredit); // Возвращаем статус 201 Created
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

    // Оплата кредита
    @PostMapping("/makePayment/{creditId}")
    public ResponseEntity<Void> makePayment(@PathVariable Long creditId, @RequestParam BigDecimal paymentAmount) {
        creditCreationService.makePayment(creditId, paymentAmount);
        return ResponseEntity.ok().build();
    }

    // Обработка исключений
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}