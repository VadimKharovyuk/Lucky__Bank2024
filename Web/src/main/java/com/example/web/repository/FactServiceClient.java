package com.example.web.repository;

import com.example.web.dto.FactDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "Fact-Service", url = "http://localhost:1000")
public interface FactServiceClient {

    @GetMapping("/facts/random")
    FactDto getRandomFact(@RequestParam("type") FactDto.FactType type);
}


