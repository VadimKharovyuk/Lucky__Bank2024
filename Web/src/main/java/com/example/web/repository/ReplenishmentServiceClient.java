package com.example.web.repository;

import com.example.web.Request.ReplenishCardRequest;
import com.example.web.dto.ReplenishmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ReplenishmentService", url = "http://localhost:1000")
public interface ReplenishmentServiceClient {


    @PostMapping("/api/replenishments/replenish")
    ResponseEntity<String> replenishCard(@RequestBody ReplenishCardRequest cardRequest);

    @GetMapping("/api/replenishments/history/{cardId}")
    List<ReplenishmentDto> getReplenishmentHistory(@PathVariable Long cardId);


}
