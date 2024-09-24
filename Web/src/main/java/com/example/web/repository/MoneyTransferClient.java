package com.example.web.repository;

import com.example.web.Request.MoneyTransferRequest;
import com.example.web.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "MoneyTransferService", url = "http://192.168.1.105:1000")
public interface MoneyTransferClient {

    @PostMapping("/api/transfer")
    TransactionDto transferMoney(@RequestBody MoneyTransferRequest moneyTransferRequest);




}
