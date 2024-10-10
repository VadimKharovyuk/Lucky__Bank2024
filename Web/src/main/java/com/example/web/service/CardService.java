package com.example.web.service;

import com.example.web.Request.CreateCardRequest;
import com.example.web.dto.CardDTO;
import com.example.web.repository.CardFeignClient;
import com.example.web.repository.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {


    private final UserFeignClient userFeignClient;
    private final CardFeignClient cardFeignClient;


    public CardDTO findByIdCard(Long id) {
        return cardFeignClient.findByIdCard(id);

    }


    public CardDTO createCard(CreateCardRequest CreateCardRequest) {
        userFeignClient.getById(CreateCardRequest.getUserId());
        return cardFeignClient.createCard(CreateCardRequest);
    }


    public List<CardDTO> getCardsByUserId(Long userId) {
    return   cardFeignClient.getCardsByUserId(userId);

    }
    public List<CardDTO> getAllCards (){
        return cardFeignClient.getAllCards();
    }
    public void deleteCardById(Long id){
        cardFeignClient.deleteCardById(id);
    }


    public BigDecimal getBalance(Long userId){
        return cardFeignClient.getTotalBalance(userId);
    }




}
