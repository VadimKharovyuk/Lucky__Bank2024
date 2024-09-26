package com.example.apipaymentservice.controler;

import com.example.apipaymentservice.model.Project;
import com.example.apipaymentservice.request.PaymentRequest;
import com.example.apipaymentservice.service.NumberParser;
import com.example.apipaymentservice.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {


    private final ProjectService projectService;

    @PostMapping("/payment")
    public ResponseEntity<String> makePayment(@RequestHeader("api-key") String apiKey, @RequestBody PaymentRequest paymentRequest) {
        // Валидация номера карты
        try {
            NumberParser.parseToSixteenDigitNumber(paymentRequest.getCardNumber());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Ошибка в номере карты: " + e.getMessage());
        }

        // Валидация CVV
        try {
            NumberParser.validateCvv(paymentRequest.getCvv());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Ошибка в CVV: " + e.getMessage());
        }

        Project project = projectService.getProjectByApiKey(apiKey);

        // Проверяем, что проект существует
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Проект не найден");
        }

        // Используем токены для оплаты
        boolean tokenUsed = projectService.useToken(project);

        if (tokenUsed) {
            // Здесь вы можете добавить логику для выполнения оплаты
            return ResponseEntity.ok("Оплата прошла успешно");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Недостаточно токенов для выполнения операции.");
        }
    }

}