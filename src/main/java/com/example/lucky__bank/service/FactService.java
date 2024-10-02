package com.example.lucky__bank.service;

import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.dto.FactDto;
import com.example.lucky__bank.maper.FactMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Fact;
import com.example.lucky__bank.repository.FactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FactService {
    private final FactRepository factRepository;
    private final FactMapper factMapper;


    // Метод для получения случайного факта по типу
    public FactDto getRandomFact(Fact.FactType type) {
        List<Fact> facts = factRepository.findByType(type);

        if (facts.isEmpty()) {
            throw new IllegalArgumentException("Нет доступных фактов для типа: " + type);
        }

        // Генерация случайного индекса
        int randomIndex = new Random().nextInt(facts.size());
        Fact randomFact = facts.get(randomIndex);
        return factMapper.toDto(randomFact);
    }




}