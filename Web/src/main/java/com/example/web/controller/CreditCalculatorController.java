package com.example.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreditCalculatorController {

    @GetMapping("/credit-calculator")
    public String showCreditCalculator() {
        return "/user/credit/calculateMonthlyPayment"; // Это имя вашего HTML-файла без расширения
    }
}
