package com.example.apipaymentservice.controler;

import com.example.apipaymentservice.model.Project;
import com.example.apipaymentservice.repository.ProjectRepository;
import com.example.apipaymentservice.request.PaymentRequest;
import com.example.apipaymentservice.service.ApiKeyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {


    private  final  ProjectRepository projectRepository;
    private final ApiKeyFilter apiKeyFilter;

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestHeader("X-API-KEY") String apiKey,
                                                 @RequestBody PaymentRequest paymentRequest) {

        // Поиск проекта по API-ключу
        Project project = projectRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный API ключ"));

        // Если тип токенов лимитированный, уменьшаем количество токенов
        if (project.getTokenType() == Project.TokenType.LIMITED) {
            if (project.getTokens() <= 0) {
                return new ResponseEntity<>("Лимит токенов исчерпан", HttpStatus.PAYMENT_REQUIRED);
            }
            project.setTokens(project.getTokens() - 1);
            projectRepository.save(project); // Обновляем количество токенов в базе данных
        }

        // Логика обработки платежа
        // Например, сохранение транзакции или взаимодействие с платежной системой

        return new ResponseEntity<>("Оплата успешно обработана", HttpStatus.OK);
    }
}