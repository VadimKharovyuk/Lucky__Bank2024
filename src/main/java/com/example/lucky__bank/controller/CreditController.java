package com.example.lucky__bank.controller;

import com.example.lucky__bank.Exception.CreditNotFoundException;
import com.example.lucky__bank.Exception.InsufficientFundsException;
import com.example.lucky__bank.Request.CreditRequestDto;
import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.dto.PaymentScheduleDto;
import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.service.CardService;
import com.example.lucky__bank.service.CreditCreationService;
import com.example.lucky__bank.service.CreditService;
import com.example.lucky__bank.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
@Slf4j
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
    public ResponseEntity<?> makePayment(@PathVariable Long creditId, @RequestParam BigDecimal paymentAmount) {
        try {
            creditCreationService.makePayment(creditId, paymentAmount);
            return ResponseEntity.ok().body("Payment processed successfully");
        } catch (InsufficientFundsException e) {
            log.warn("Insufficient funds for payment on credit ID: {}", creditId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient funds on the linked card");
        } catch (CreditNotFoundException e) {
            log.error("Credit not found or error processing payment for credit ID: {}", creditId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error when processing payment for credit ID: {}", creditId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }



    //удалить
    @PostMapping("/delete/{creditId}")
    public ResponseEntity<Void> deleteCreditId(@PathVariable Long creditId){
        creditService.deleteCredit(creditId);
        return ResponseEntity.noContent().build();
    }


//cписок кредитов юзера по карте
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


    @GetMapping("/schedule")
    public ResponseEntity<List<PaymentScheduleDto>> getAllByCredit(@RequestParam Long creditId) {
        CreditDto creditDto = new CreditDto();
        creditDto.setId(creditId);

        List<PaymentScheduleDto> schedules = creditService.getAllByCredit(creditDto);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/schedule/{creditId}")
    public ResponseEntity<PaymentScheduleDto> getCreditById(@PathVariable Long creditId){
       PaymentScheduleDto scheduleDto = creditService.getSchdulerByid(creditId);
        return ResponseEntity.ok(scheduleDto);
    }



    // Обработка исключений
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}