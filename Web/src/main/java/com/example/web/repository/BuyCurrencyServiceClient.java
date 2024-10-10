package com.example.web.repository;

import com.example.web.Request.CurrencyPurchaseRequest;
import com.example.web.dto.CardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "BuyCurrency-ServiceClient", url = "http://localhost:1000")
public interface BuyCurrencyServiceClient {


    @PostMapping("/api/currency/buy")
    CardDTO buyCurrency(@RequestBody CurrencyPurchaseRequest request);
}
