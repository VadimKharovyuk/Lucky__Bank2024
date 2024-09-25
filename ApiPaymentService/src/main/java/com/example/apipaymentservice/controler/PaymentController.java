package com.example.apipaymentservice.controler;

import com.example.apipaymentservice.model.Project;
import com.example.apipaymentservice.request.PaymentRequest;
import com.example.apipaymentservice.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final ProjectService projectService;

    @PostMapping("/pay")
    public ResponseEntity<String> makePayment(@RequestHeader("api-key") String apiKey, @RequestBody PaymentRequest paymentRequest) {
        Project project = projectService.getProjectByApiKey(apiKey);

        // Проверяем, что проект существует
        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Проект не найден");
        }

        // Используем токены для оплаты
        boolean tokenUsed = projectService.useToken(project);

        if (tokenUsed) {
            // Здесь вы можете добавить логику для выполнения оплаты
            // Например, вызвать метод для обработки самой оплаты (если у вас есть такая логика)
            // boolean paymentSuccess = paymentService.processPayment(paymentRequest);

            // Предположим, что оплата прошла успешно
            return ResponseEntity.ok("Оплата прошла успешно");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Недостаточно токенов для выполнения операции.");
        }
    }

//    @PostMapping("/{apiKey}/pay")
//    public ResponseEntity<String> makePayment(@PathVariable String apiKey, @RequestBody PaymentRequest paymentRequest) {
//        Project project = projectService.getProjectByApiKey(apiKey);
//
//        // Проверяем, что проект существует
//        if (project == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Проект не найден");
//        }
//
//        // Используем токены для оплаты
//        boolean tokenUsed = projectService.useToken(project);
//
//        if (tokenUsed) {
//            // Здесь вы можете добавить логику для выполнения оплаты
//            // Например, вызвать метод для обработки самой оплаты (если у вас есть такая логика)
//            // boolean paymentSuccess = paymentService.processPayment(paymentRequest);
//
//            // Предположим, что оплата прошла успешно
//            return ResponseEntity.ok("Оплата прошла успешно");
//        } else {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Недостаточно токенов для выполнения операции.");
//        }
//    }
}