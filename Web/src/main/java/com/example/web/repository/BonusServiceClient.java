package com.example.web.repository;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;


@FeignClient(name = "Bonus-Service", url = "http://localhost:1000")
public interface BonusServiceClient {

    @PostMapping("/api/bonus/claim/{cardId}")
    ResponseEntity<Map<String, String>> claimDailyBonus(@PathVariable Long cardId);

}


