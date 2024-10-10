package com.example.web.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.web.repository.CurrencyServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CurrencyService {

    private final CurrencyServiceClient currencyServiceClient ;

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
         return currencyServiceClient.convert(amount, fromCurrency, toCurrency);
    }


    public List<String> getCurrencyRates() {
       return currencyServiceClient.getCurrencyRates();
    }

}