package com.example.lucky__bank.service;

import com.example.lucky__bank.Exception.EntityNotFoundException;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BirthdayService {


    public Optional<String> checkBirthday(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return Optional.empty();
        }

        LocalDate today = LocalDate.now();
        long daysUntilBirthday = ChronoUnit.DAYS.between(today, dateOfBirth.withYear(today.getYear()));

        if (daysUntilBirthday < 0) {
            daysUntilBirthday = ChronoUnit.DAYS.between(today, dateOfBirth.withYear(today.getYear() + 1));
        }

        if (daysUntilBirthday == 0) {
            return Optional.of("Поздравляем с Днём Рождения!");
        } else if (daysUntilBirthday <= 5) {
            return Optional.of("Ваш День Рождения через " + daysUntilBirthday + " дня(дней)!");
        }

        return Optional.empty();
    }
}
