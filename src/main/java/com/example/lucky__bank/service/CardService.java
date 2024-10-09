package com.example.lucky__bank.service;

import com.example.lucky__bank.Exception.InsufficientFundsException;
import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.maper.CardMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.CardRepository;
import com.example.lucky__bank.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private  final  CardRepository cardRepository;
    private  final  UserRepository userRepository;
    private final CardMapper cardMapper;


    public CardDTO createCard(Long userId, String cardType) {
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

    public CardDTO findById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with ID: " + id));
        return cardMapper.toDTO(card);
    }

    public List<CardDTO> getCardsByUserId(Long userId) {
        List<Card> cards = cardRepository.findByUserId(userId);
        return cards.stream()
                .map(cardMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<CardDTO>getAllCard(){
       List<Card> card = cardRepository.findAll();
       return card.stream().map(cardMapper::toDTO)
               .collect(Collectors.toList());
    }
    public CardDTO deleteCardById(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Карта с ID " + id + " не найдена"));

        cardRepository.deleteById(id);

        return cardMapper.toDTO(card);
    }

    public Card findEntityById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with ID: " + id));
    }

    //снимать деньги с карты на кредит
    @Transactional
    public void withdrawMoney(Long cardId, BigDecimal amount) throws InsufficientFundsException {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found for ID: " + cardId));

        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds on the card");
        }

        card.setBalance(card.getBalance().subtract(amount));
        cardRepository.save(card);
    }

    public BigDecimal getTotalBalanceByUser(User user) {
        List<Card> cards = cardRepository.findAll();
        return cards.stream()
                .filter(card -> card.getUser().equals(user)) // Фильтруем по пользователю
                .map(Card::getBalance)                      // Извлекаем баланс из каждой карты
                .reduce(BigDecimal.ZERO, BigDecimal::add);   // Складываем все балансы
    }
}