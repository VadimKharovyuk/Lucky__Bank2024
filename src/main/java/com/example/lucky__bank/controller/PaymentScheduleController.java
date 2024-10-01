package com.example.lucky__bank.controller;

import com.example.lucky__bank.dto.PaymentScheduleDto;
import com.example.lucky__bank.maper.PaymentScheduleMapper;
import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.repository.PaymentScheduleRepository;
import com.example.lucky__bank.service.Schedule.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment-schedules")
@RequiredArgsConstructor
public class PaymentScheduleController {
    private final PaymentService paymentService;



    @GetMapping("/credit/{creditId}")
    public ResponseEntity<List<PaymentScheduleDto>> getPaymentSchedulesByCreditId(@PathVariable Long creditId) {
        List<PaymentScheduleDto> paymentScheduleDtos = paymentService.getPaymentSchedulesByCreditId(creditId);
        return ResponseEntity.ok(paymentScheduleDtos);
    }
}