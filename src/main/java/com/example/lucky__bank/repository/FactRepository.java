package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.Fact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FactRepository extends JpaRepository<Fact,Long> {
    List<Fact> findByType(Fact.FactType type);

}
