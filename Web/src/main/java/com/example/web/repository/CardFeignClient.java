package com.example.web.repository;

import com.example.web.dto.CardDTO;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "lucky-bank-card-service", url = "http://localhost:1000")
public interface CardFeignClient {

    @GetMapping("/api/cards/all")
    List<CardDTO> getAllCards();

    @GetMapping("/api/cards/user/{id}")
    List<CardDTO> getCardsByUserId(@PathVariable("id") Long id);
    @GetMapping("/api/cards/{id}")
    CardDTO findByIdCard(@PathVariable("id") Long id);

    @PostMapping("/api/cards")
    CardDTO createCard(@RequestParam("userId") Long userId, @RequestParam("cardType") String cardType);


}
