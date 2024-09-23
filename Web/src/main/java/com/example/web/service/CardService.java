package com.example.web.service;

import com.example.web.dto.CardDTO;
import com.example.web.repository.CardFeignClient;
import com.example.web.repository.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {


    private final UserFeignClient userFeignClient;
    private final CardFeignClient cardFeignClient;


    public CardDTO findByIdCard(Long id) {
        return cardFeignClient.findByIdCard(id);
    }

    public CardDTO createCard(Long userId, String cardType) {
        userFeignClient.getById(userId);
        return cardFeignClient.createCard(userId, cardType);
    }


    public List<CardDTO> getCardsByUserId(Long userId) {
    return   cardFeignClient.getCardsByUserId(userId);

    }
}
