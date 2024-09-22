package com.example.lucky__bank.service;

import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.maper.CardMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.CardRepository;
import com.example.lucky__bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardMapper cardMapper;


    public CardDTO createCard(Long userId, String cardType) {
        // Найти пользователя по userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Создать новую карту
        Card card = new Card();
        card.setCardNumber(generateCardNumber());
        card.setCardType(Card.CardType.valueOf(cardType));
        card.setBalance(BigDecimal.ZERO); // Устанавливаем баланс по умолчанию на 0
        card.setCreatedAt(LocalDateTime.now());
        card.setExpirationDate(LocalDateTime.now().plus(5, ChronoUnit.YEARS));
        card.setCvv(generateCvv());
        card.setUser(user); // Устанавливаем связь с пользователем

        // Сохранить карту в базе данных
        cardRepository.save(card);

        // Возвращаем DTO карты
        return cardMapper.toDTO(card);
    }

    private String generateCardNumber() {
        // Пример генерации 16-значного номера карты
        return String.valueOf(1000000000000000L + (long) (Math.random() * 9000000000000000L));
    }
    private String generateCvv() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(1000)); // Генерация трехзначного числа
    }
}