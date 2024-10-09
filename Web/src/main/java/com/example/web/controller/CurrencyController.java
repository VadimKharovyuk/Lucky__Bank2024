package com.example.web.controller;

import com.example.web.service.CurrencyRate;
import com.example.web.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

//
//    @GetMapping
//    public String showCurrencyConverter(Model model) {
//        List<CurrencyRate> rates = currencyService.getCurrencyRates();
//        model.addAttribute("rates", rates);
//        model.addAttribute("currencies", Arrays.asList("UAH", "USD", "EUR"));
//        return "user/currency/converter";
//    }

    @PostMapping("/convert")
    @ResponseBody
    public BigDecimal convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam String fromCurrency,
            @RequestParam String toCurrency) {
        return currencyService.convert(amount, fromCurrency, toCurrency);
    }
}