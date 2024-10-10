package com.example.lucky__bank.service;

import com.example.lucky__bank.Exception.InsufficientFundsException;
import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.maper.CardMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.CardRepository;
import com.example.lucky__bank.repository.UserRepository;
import com.example.lucky__bank.service.api.CurrencyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private  final  CardRepository cardRepository;
    private  final  UserRepository userRepository;
    private final CardMapper cardMapper;
    private final CurrencyService currencyService;


    @Transactional
    public CardDTO buyCurrency(Long userId, Long cardId, BigDecimal amount, String fromCurrency, String toCurrency)
            throws InsufficientFundsException, IllegalArgumentException {
        log.info("Starting currency purchase: userId={}, cardId={}, amount={}, fromCurrency={}, toCurrency={}",
                userId, cardId, amount, fromCurrency, toCurrency);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        if (!card.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Card does not belong to the user");
        }

        BigDecimal convertedAmount = currencyService.convert(amount, fromCurrency, toCurrency);
        log.info("Initial card balance: {}", card.getBalance());
        log.info("Converted amount: {}", convertedAmount);
        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for currency purchase");
        }

        // Уменьшаем баланс исходной карты
        card.setBalance(card.getBalance().subtract(amount));
        cardRepository.save(card);
        log.info("Updated source card balance: {}", card.getBalance());

        // Находим или создаем карту для целевой валюты
        Card targetCard = (Card) cardRepository.findByUserAndCardType(user, Card.CardType.valueOf(toCurrency))
                .orElseGet(() -> {
                    CardDTO newCardDTO = createCard(userId, toCurrency);
                    return cardRepository.findById(newCardDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Failed to create new currency card"));
                });

        // Увеличиваем баланс целевой карты
        targetCard.setBalance(targetCard.getBalance().add(convertedAmount));
        cardRepository.save(targetCard);
        log.info("Updated target card balance: {}", targetCard.getBalance());

        CardDTO result = cardMapper.toDTO(targetCard);
        log.info("Returning result: {}", result);
        return result;
    }


    @Transactional
    public CardDTO createCard(Long userId, String cardType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Card card = new Card();
        card.setCardNumber(generateCardNumber());
        card.setCardType(Card.CardType.valueOf(cardType));
        card.setBalance(BigDecimal.ZERO);
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        card.setExpirationDate(LocalDateTime.now().plusYears(5));
        card.setCvv(generateCvv());
        card.setUser(user);
        card.setLastBonusDate(LocalDate.now().minusDays(1));

        cardRepository.save(card);

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