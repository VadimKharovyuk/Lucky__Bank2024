
package com.example.lucky__bank.service;
import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.dto.TransactionDto;
import com.example.lucky__bank.maper.TransactionMapper;

import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Transaction;
import com.example.lucky__bank.repository.CardRepository;
import com.example.lucky__bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CardRepository cardRepository;

    @Transactional
    public TransactionDto transferMoney(String fromCardNumber, String toCardNumber, BigDecimal amount, String description) {

        Card fromCard = cardRepository.findByCardNumber(fromCardNumber)
                .orElseThrow(() -> new RuntimeException("Карта отправителя не найдена"));


        Card toCard = cardRepository.findByCardNumber(toCardNumber)
                .orElseThrow(() -> new RuntimeException("Карта получателя не найдена"));


        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new ArithmeticException("Недостаточно средств на карте отправителя");
        }

        // Выполнение перевода
        performTransfer(fromCard, toCard, amount);

        // Создание и сохранение транзакции
        Transaction savedTransaction = createTransaction(fromCardNumber, toCardNumber, amount, description);


        return transactionMapper.toDTO(savedTransaction);
    }

    private void performTransfer(Card fromCard, Card toCard, BigDecimal amount) {
        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));
        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }

    private Transaction createTransaction(String fromCardNumber, String toCardNumber, BigDecimal amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setFromCardNumber(fromCardNumber);
        transaction.setToCardNumber(toCardNumber);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription(description);
        return transactionRepository.save(transaction);
    }
}