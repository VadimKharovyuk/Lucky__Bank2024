package com.example.lucky__bank.service;

import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BonusService {
    private final CardRepository cardRepository;

    @Transactional
    public BigDecimal addDailyBonus(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        LocalDate today = LocalDate.now();
        if (card.getLastBonusDate() != null && card.getLastBonusDate().equals(today)) {
            throw new IllegalStateException("Bonus already received today");
        }

        BigDecimal bonusAmount = generateRandomBonus();
        card.setBalance(card.getBalance().add(bonusAmount));
        card.setLastBonusDate(today);
        cardRepository.save(card);

        return bonusAmount;
    }

    private BigDecimal generateRandomBonus() {
        Random random = new Random();
        int bonus = random.nextInt(96) + 5; // От 5 до 100
        return new BigDecimal(bonus);
    }
}