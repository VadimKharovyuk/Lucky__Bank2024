package com.example.web.service;

import com.example.web.Request.CurrencyPurchaseRequest;
import com.example.web.dto.CardDTO;
import com.example.web.exeption.CustomServiceException;
import com.example.web.repository.BuyCurrencyServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyCurrencyService {

    private final BuyCurrencyServiceClient buyCurrencyServiceClient;

    public CardDTO buyCurrency(CurrencyPurchaseRequest request) {
        log.info(" карта с id : {}", request.getCardId());
        log.info("Отправка запроса на покупку валюты: {}", request);
        try {
            CardDTO response = buyCurrencyServiceClient.buyCurrency(request);
            log.info("Получен ответ: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Ошибка при покупке валюты: {}", e.getMessage());
            throw new RuntimeException("Ошибка при покупке валюты", e);
        }
    }

}
