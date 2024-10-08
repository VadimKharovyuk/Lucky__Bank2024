package com.example.lucky__bank.service;

import com.example.lucky__bank.dto.DepositDto;
import com.example.lucky__bank.maper.DepositMapper;
import com.example.lucky__bank.model.Deposit;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.repository.CardRepository;
import com.example.lucky__bank.repository.DepositRepository;
import com.example.lucky__bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;
    private final DepositMapper depositMapper;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    // Процентная ставка (2%)
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.02");

    @Transactional
    public DepositDto createDeposit(Long userId, Long cardId, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        // Проверка, что карта принадлежит пользователю
        if (!card.getUser().equals(user)) {
            throw new IllegalArgumentException("This card does not belong to the user.");
        }

        // Проверка, достаточно ли средств на карте
        if (card.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds on the card.");
        }

        // Снимаем деньги с карты
        card.setBalance(card.getBalance().subtract(amount));
        cardRepository.save(card);

        // Создаем новый депозит
        Deposit deposit = new Deposit();
        deposit.setUser(user);
        deposit.setCard(card);
        deposit.setAmount(amount);
        deposit.setCreatedAt(LocalDateTime.now());
        deposit.setUpdatedAt(LocalDateTime.now());


        depositRepository.save(deposit);
        return depositMapper.toDto(deposit);
    }

    @Transactional(readOnly = true)
    public DepositDto getDepositById(Long depositId) {
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new IllegalArgumentException("Deposit not found"));

        return depositMapper.toDto(deposit);
    }

    @Transactional
    public DepositDto withdrawAllFromDeposit(Long userId, Long cardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        // Получаем список депозитов пользователя и карты
        List<Deposit> deposits = depositRepository.findByUserIdAndCardId(userId, cardId);

        // Проверяем, есть ли депозиты
        if (deposits.isEmpty()) {
            throw new IllegalArgumentException("No active deposits found for user and card");
        }

        // Предположим, что мы хотим снять средства с первого депозита в списке
        Deposit deposit = deposits.get(0);

        // Получаем полную сумму депозита
        BigDecimal amountToWithdraw = deposit.getAmount();

        // Снимаем средства с депозита и возвращаем на карту
        card.setBalance(card.getBalance().add(amountToWithdraw));

        // Удаляем депозит из базы данных
        depositRepository.delete(deposit); // Удаляем депозит

        // Обновляем время последнего изменения карты
        card.setUpdatedAt(LocalDateTime.now());
        cardRepository.save(card);
        return depositMapper.toDto(deposit);
    }


    @Transactional(readOnly = true)
    public List<DepositDto> findByUserAndCard(Long userId, Long cardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Проверяем существование карты по ID
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        // Поиск депозитов по пользователю и карте
        List<Deposit> deposits = depositRepository.findByUserIdAndCardId(userId, cardId);

        return deposits.stream()
                .map(depositMapper::toDto)
                .collect(Collectors.toList());
    }


    // Метод для начисления процентов на все депозиты
    @Transactional
    public void applyDailyInterest() {
        List<Deposit> deposits = depositRepository.findAll();

        for (Deposit deposit : deposits) {
            BigDecimal interest = deposit.getAmount().multiply(INTEREST_RATE);
            deposit.setAmount(deposit.getAmount().add(interest));
            deposit.setUpdatedAt(LocalDateTime.now());
            depositRepository.save(deposit);
        }
    }
}