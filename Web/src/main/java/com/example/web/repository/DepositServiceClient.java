package com.example.web.repository;

import com.example.web.dto.DepositDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "DepositService", url = "http://localhost:1000")
public interface DepositServiceClient {

    @PostMapping("/api/deposits/create")
    DepositDto create(@RequestParam("userId") Long userId,
                      @RequestParam("cardId") Long cardId,
                      @RequestParam("amount") BigDecimal amount);
    @GetMapping("/api/deposits/{depositId}")
    DepositDto getDepositById(@PathVariable Long depositId);

    @PostMapping("/api/deposits/withdraw-all")
    DepositDto withdrawAllFromDeposit (@RequestParam Long userId ,
                                       @RequestParam Long cardId);


    @GetMapping("/api/deposits/find")
    List<DepositDto>findDepositsByUserAndCard (@RequestParam Long userId,
                                               @RequestParam Long cardId);


}


