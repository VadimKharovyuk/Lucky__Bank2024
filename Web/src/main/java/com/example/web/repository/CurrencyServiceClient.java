package com.example.web.repository;

import com.example.web.service.CurrencyRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "Currency-Service", url = "http://localhost:1000")
public interface CurrencyServiceClient {
    @PostMapping("/api/convert/convert")
    BigDecimal convert(@RequestParam("amount") BigDecimal amount,
                       @RequestParam("fromCurrency") String fromCurrency,
                       @RequestParam("toCurrency") String toCurrency);

    @GetMapping("/api/convert/all")
    List<String> getCurrencyRates();

}
