package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.CreditRequestDto;
import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.service.CardService;
import com.example.lucky__bank.service.CreditCreationService;
import com.example.lucky__bank.service.CreditService;
import com.example.lucky__bank.service.UserService;
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
    public ResponseEntity<CreditDto> createCredit(@RequestBody  CreditRequestDto requestDto) {
        CreditDto createdCredit = creditCreationService.createCredit(
                requestDto.getUserId(),
                requestDto.getCardId(),
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
        try {
            creditCreationService.makePayment(creditId, paymentAmount);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Логируем ошибку
            System.err.println("Ошибка при обработке платежа: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    //удалить
    @PostMapping("/delete/{creditId}")
    public ResponseEntity<Void> deleteCreditId(@PathVariable Long creditId){
        creditService.deleteCredit(creditId);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/list")
    public ResponseEntity<List<CreditDto>> getCreditsByUserAndCard(
            @RequestParam Long userId,
            @RequestParam Long cardId) {

        UserDTO userDto = new UserDTO();
        userDto.setId(userId);

        CardDTO cardDto = new CardDTO();
        cardDto.setId(cardId);

        List<CreditDto> credits = creditService.getCreditsByUserAndCard(userDto, cardDto);
        return ResponseEntity.ok(credits);
    }

    // Обработка исключений
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}