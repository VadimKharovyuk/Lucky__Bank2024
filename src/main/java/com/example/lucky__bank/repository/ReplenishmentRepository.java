package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.Replenishment;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplenishmentRepository extends JpaRepository<Replenishment,Long> {

    List<Replenishment> findAllByCardId(Long cardId);

}
