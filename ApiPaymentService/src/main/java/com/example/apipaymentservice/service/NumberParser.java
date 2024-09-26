package com.example.apipaymentservice.service;

import org.springframework.stereotype.Service;

@Service
public class NumberParser {

    // Метод для проверки валидности номера карты (16 цифр)
    public static void parseToSixteenDigitNumber(String input) throws IllegalArgumentException {
        if (input == null || input.isEmpty() || !input.matches("\\d+")) {
            throw new IllegalArgumentException("Номер карты содержит недопустимые символы или пуст.");
        }

        // Преобразуем строку в число
        if (input.length() != 16) {
            throw new IllegalArgumentException("Номер карты должен содержать 16 цифр.");
        }
    }

    // Метод для проверки валидности CVV
    public static void validateCvv(String cvv) throws IllegalArgumentException {
        // Проверяем, что строка содержит ровно 3 цифры
        if (cvv == null || !cvv.matches("\\d{3}")) {
            throw new IllegalArgumentException("CVV должен состоять из трёх цифр.");
        }
    }
}