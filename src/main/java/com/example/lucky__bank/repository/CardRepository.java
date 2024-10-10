package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.User;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Long> {
    Optional<Card> findById (Long id);

    List<Card> findByUserId(Long userId);

    // Правильное имя метода
    Optional<Card> findByCardNumber(String cardNumber);


    <T> Optional<T> findByUserAndCardType(User user, Card.CardType cardType);

}
