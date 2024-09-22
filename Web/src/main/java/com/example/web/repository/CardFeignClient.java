package com.example.web.repository;

import com.example.web.dto.CardDTO;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "lucky-bank-card-service", url = "http://192.168.1.105:1000")
public interface CardFeignClient {

    @PostMapping("/api/cards")
    CardDTO createCard(@RequestParam("userId") Long userId, @RequestParam("cardType") String cardType);
}
