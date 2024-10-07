package com.example.lucky__bank.service;

import com.example.lucky__bank.Request.ReplenishCardRequest;
import com.example.lucky__bank.dto.ReplenishmentDto;
import com.example.lucky__bank.maper.ReplenishmentMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Replenishment;
import com.example.lucky__bank.repository.CardRepository;
import com.example.lucky__bank.repository.ReplenishmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplenishmentService {
    private final ReplenishmentRepository replenishmentRepository;
    private final ReplenishmentMapper replenishmentMapper ;
    private final CardRepository cardRepository ;


    @Transactional
    public void replenishCard(String  carNumber, BigDecimal amount) {
        Card card = cardRepository.findByCardNumber(carNumber)
                .orElseThrow(() -> new IllegalArgumentException("Карта " +carNumber + " не найдена"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода  должна быть больше 0");
        }

        // Пополняем баланс карты
        card.setBalance(card.getBalance().add(amount));
        card.setUpdatedAt(LocalDateTime.now());
        cardRepository.save(card);

        // Используем маппер для создания записи о пополнении
        Replenishment replenishment = replenishmentMapper.toEntity(
                ReplenishmentDto.builder()
                        .cardNumber(String.valueOf(card.getId()))
                        .amount(amount)
                        .createdAt(LocalDateTime.now())
                        .build(),
                card
        );

        replenishmentRepository.save(replenishment);
    }

    // список пополнений по карте
    public List<ReplenishmentDto> getReplenishmentsByCard(Long cardId) {
        List<Replenishment> replenishments = replenishmentRepository.findAllByCardId(cardId);

        // Проверяем, были ли найдены пополнения
        if (replenishments == null || replenishments.isEmpty()) {
            throw new EntityNotFoundException("Пополнения для данной карты не найдены");
        }

        // Преобразуем список пополнений в DTO
        return replenishments.stream()
                .map(replenishmentMapper::toDto)
                .collect(Collectors.toList());
    }

}
