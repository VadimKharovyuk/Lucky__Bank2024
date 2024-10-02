package com.example.lucky__bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Fact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 300)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FactType type;



    public enum FactType {
        JAVA,
        UKRAINE,
        MISC;

    }
}
