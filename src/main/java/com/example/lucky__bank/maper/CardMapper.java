package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.model.Card;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardDTO toDTO(Card card) {
        return CardDTO.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .cardType(card.getCardType().name())
                .balance(card.getBalance())
                .createdAt(card.getCreatedAt())
                .expirationDate(card.getExpirationDate())
                .cvv(card.getCvv())
                .build();
    }

    public Card toEntity(CardDTO cardDTO) {
        Card card = new Card();
        card.setId(cardDTO.getId());
        card.setCardNumber(cardDTO.getCardNumber());
        card.setCardType(Card.CardType.valueOf(cardDTO.getCardType()));
        card.setBalance(cardDTO.getBalance());
        card.setCreatedAt(cardDTO.getCreatedAt());
        card.setExpirationDate(cardDTO.getExpirationDate());
        card.setCvv(cardDTO.getCvv());
        return card;
    }
}