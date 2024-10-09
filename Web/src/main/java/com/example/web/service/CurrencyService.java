package com.example.web.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {
    private static final String API_URL = "https://api.privatbank.ua/p24api/pubinfo?exchange&json&coursid=5";
    private static final String BASE_CURRENCY = "UAH";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public CurrencyService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<CurrencyRate> getCurrencyRates() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);
            return objectMapper.readValue(response.getBody(), new TypeReference<List<CurrencyRate>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch currency rates", e);
        }
    }

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        List<CurrencyRate> rates = getCurrencyRates();

        if (fromCurrency.equals(BASE_CURRENCY)) {
            CurrencyRate toRate = findRate(rates, toCurrency);
            return amount.divide(toRate.getBuyRate(), 2, RoundingMode.HALF_UP);
        } else if (toCurrency.equals(BASE_CURRENCY)) {
            CurrencyRate fromRate = findRate(rates, fromCurrency);
            return amount.multiply(fromRate.getSaleRate());
        } else {
            CurrencyRate fromRate = findRate(rates, fromCurrency);
            CurrencyRate toRate = findRate(rates, toCurrency);
            BigDecimal amountInUAH = amount.multiply(fromRate.getSaleRate());
            return amountInUAH.divide(toRate.getBuyRate(), 2, RoundingMode.HALF_UP);
        }
    }

    private CurrencyRate findRate(List<CurrencyRate> rates, String currency) {
        return rates.stream()
                .filter(r -> r.getCcy().equals(currency))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Currency not found: " + currency));
    }
}