package com.example.web.service;

import com.example.web.dto.CardDTO;
import com.example.web.repository.CardFeignClient;
import com.example.web.repository.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {



    private final UserFeignClient userFeignClient;
    private final CardFeignClient cardFeignClient;

    public CardDTO createCard(Long userId, String cardType) {
        userFeignClient.getById(userId);
        return cardFeignClient.createCard(userId, cardType);
    }
}
