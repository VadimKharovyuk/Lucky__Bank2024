package com.example.web.repository;

import com.example.web.dto.PaymentScheduleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "PaymentService", url = "http://localhost:1000")
public interface PaymentScheduleClient {

    @GetMapping("/api/payment-schedules/credit/{creditId}")
    List<PaymentScheduleDto>getPaymentSchedulesByCreditId(@PathVariable Long creditId);


}
