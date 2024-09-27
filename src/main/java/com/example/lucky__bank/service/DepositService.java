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
        // Ищем пользователя по ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Ищем карту по ID
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
        cardRepository.save(card);  // Сохраняем изменения баланса карты

        // Создаем новый депозит
        Deposit deposit = new Deposit();
        deposit.setUser(user);
        deposit.setCard(card);
        deposit.setAmount(amount);
        deposit.setCreatedAt(LocalDateTime.now());
        deposit.setUpdatedAt(LocalDateTime.now());

        // Сохраняем депозит в базе данных
        depositRepository.save(deposit);

        // Преобразуем и возвращаем DTO
        return depositMapper.toDto(deposit);
    }

    @Transactional(readOnly = true)
    public DepositDto getDepositById(Long depositId) {
        // Получаем депозит из базы данных
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new IllegalArgumentException("Deposit not found"));

        // Преобразуем сущность в DTO и возвращаем
        return depositMapper.toDto(deposit);
    }

    @Transactional
    public DepositDto withdrawAllFromDeposit(Long userId, Long cardId) {
        // Получаем пользователя из базы данных
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Получаем карту из базы данных
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        // Получаем депозит пользователя и карты
        Deposit deposit = depositRepository.findByUserAndCard(user, card)
                .orElseThrow(() -> new IllegalArgumentException("No active deposit found for user and card"));

        // Получаем полную сумму депозита
        BigDecimal amountToWithdraw = deposit.getAmount();

        // Снимаем средства с депозита и возвращаем на карту
        card.setBalance(card.getBalance().add(amountToWithdraw));

        // Удаляем депозит из базы данных
        depositRepository.delete(deposit); // Удаляем депозит

        // Обновляем время последнего изменения карты
        card.setUpdatedAt(LocalDateTime.now());
        cardRepository.save(card); // Сохраняем изменения в балансе карты

        // Возвращаем информацию о снятом депозите в виде DTO
        return depositMapper.toDto(deposit);
    }


    @Transactional(readOnly = true)
    public Optional<DepositDto> findByUserAndCard(Long userId, Long cardId) {
        // Получаем пользователя из базы данных
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Получаем карту из базы данных
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        // Поиск депозита по пользователю и карте
        return depositRepository.findByUserAndCard(user, card)
                .map(depositMapper::toDto);
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