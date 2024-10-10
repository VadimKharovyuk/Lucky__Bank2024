package com.example.lucky__bank.controller;

import com.example.lucky__bank.service.api.CurrencyRate;
import com.example.lucky__bank.service.api.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/convert")
public class CurrencyController {


    private final CurrencyService currencyService;


    @GetMapping("/all")
   public ResponseEntity<List<String>> getCurrencyRates (){
        List<CurrencyRate> rates = currencyService.getCurrencyRates();
        // Преобразование списка объектов CurrencyRate в список строк (например, валюты)
        List<String> currencies = rates.stream()
                .map(CurrencyRate::getCcy) // получение валюты из каждого CurrencyRate
                .toList();

        return ResponseEntity.ok(currencies);
    }

    @PostMapping("/convert")
    @ResponseBody
    public BigDecimal convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {
        return currencyService.convert(amount, fromCurrency, toCurrency);
    }
}
