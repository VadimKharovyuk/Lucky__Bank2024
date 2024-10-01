package com.example.web.repository;

import com.example.web.Request.CreditRequestDto;
import com.example.web.dto.CreditDto;
import com.example.web.dto.PaymentScheduleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "CreditService", url = "http://localhost:1000")
public interface CreditServiceClient {

    @PostMapping("/api/credits")
    CreditDto create(@RequestBody CreditRequestDto creditRequestDto);

    @GetMapping("/api/credits")
    List<CreditDto> creditList();

    // Метод для одобрения кредита
    @PutMapping("/api/credits/{creditId}/approve")
    CreditDto approveCredit(@PathVariable Long creditId);



    // Оплата кредита
    @PostMapping("/api/credits/makePayment/{creditId}")
    void makePayment(@PathVariable Long creditId, @RequestParam BigDecimal paymentAmount);


    @PostMapping("/api/credits/delete/{creditId}")
    void deleteCreditById(@PathVariable Long creditId);


    @GetMapping("/api/credits/list")
    List<CreditDto> getCreditsByUserAndCard(@RequestParam Long userId, @RequestParam Long cardId);


    @GetMapping("/api/credits/schedule")
    List<PaymentScheduleDto> getAllByCredit(@RequestParam Long creditId);

    @GetMapping("/api/credits/schedule/{creditId}")
    CreditDto getCreditById(@PathVariable Long creditId);

}
