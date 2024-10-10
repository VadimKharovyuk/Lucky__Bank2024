package com.example.web.repository;

import com.example.web.Request.CreateCardRequest;
import com.example.web.dto.CardDTO;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "lucky-bank-card-service", url = "http://localhost:1000")
public interface CardFeignClient {

    @GetMapping("/api/cards/all")
    List<CardDTO> getAllCards();

    @GetMapping("/api/cards/user/{id}")
    List<CardDTO> getCardsByUserId(@PathVariable("id") Long id);
    @GetMapping("/api/cards/{id}")
    CardDTO findByIdCard(@PathVariable("id") Long id);


    @PostMapping(value = "/api/cards", consumes = MediaType.APPLICATION_JSON_VALUE)
    CardDTO createCard(@RequestBody CreateCardRequest createCardRequest);



    @PostMapping("/api/cards/delete/{id}")
    void deleteCardById(@PathVariable Long id);

    @GetMapping("/api/cards/total-balance/user/{userId}")
    BigDecimal getTotalBalance(@PathVariable("userId") Long userId);
}


