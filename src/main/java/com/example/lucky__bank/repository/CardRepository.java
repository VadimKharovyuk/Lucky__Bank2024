package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.Card;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Long> {
    Optional<Card> findById (Long id);

    List<Card> findByUserId(Long userId);

}
