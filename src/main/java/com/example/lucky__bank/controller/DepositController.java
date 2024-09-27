package com.example.lucky__bank.controller;

import com.example.lucky__bank.dto.DepositDto;
import com.example.lucky__bank.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositService depositService;
    // Создание депозита
    @PostMapping("/create")
    public ResponseEntity<DepositDto> createDeposit(@RequestParam Long userId,
                                                    @RequestParam Long cardId,
                                                    @RequestParam BigDecimal amount) {
        DepositDto depositDto = depositService.createDeposit(userId, cardId, amount);
        return ResponseEntity.ok(depositDto);
    }

    // Получение депозита по ID
    @GetMapping("/{depositId}")
    public ResponseEntity<DepositDto> getDepositById(@PathVariable Long depositId) {
        DepositDto depositDto = depositService.getDepositById(depositId);
        return ResponseEntity.ok(depositDto);
    }

    // Снятие средств с депозита
    @PostMapping("/withdraw-all")
    public ResponseEntity<DepositDto> withdrawAllFromDeposit(@RequestParam Long userId,
                                                             @RequestParam Long cardId) {
        DepositDto depositDto = depositService.withdrawAllFromDeposit(userId, cardId);
        return ResponseEntity.ok(depositDto);
    }


    // Начисление процентов вручную (необходимо только для тестирования или административных операций)
    @PostMapping("/apply-interest")
    public ResponseEntity<Void> applyDailyInterest() {
        depositService.applyDailyInterest();
        return ResponseEntity.ok().build();
    }
    @GetMapping("/find")
    public ResponseEntity<DepositDto> findDepositByUserAndCard(@RequestParam Long userId,
                                                               @RequestParam Long cardId) {
        // Ищем депозит
        Optional<DepositDto> depositDto = depositService.findByUserAndCard(userId, cardId);

        return depositDto
                .map(ResponseEntity::ok)  // Если депозит найден, возвращаем его
                .orElseGet(() -> ResponseEntity.notFound().build());  // Если депозит не найден, возвращаем 404
    }

}