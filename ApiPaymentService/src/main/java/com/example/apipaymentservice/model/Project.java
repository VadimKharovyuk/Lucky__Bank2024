package com.example.apipaymentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private Long userId;
    private String title;
    private String description;
    private String apiKey;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_reset_date")
    private LocalDate lastResetDate;

    // Поле для хранения токенов
    private Integer tokens;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;



    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum TokenType {
        LIMITED,
        UNLIMITED // Безлимитное количество токенов
    }
}