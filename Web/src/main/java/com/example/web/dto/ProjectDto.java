package com.example.web.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class ProjectDto {
    private Long id;
    private String title;
    private String description;
    private String apiKey;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate lastResetDate;
    private Integer tokens; // Количество токенов
    private String tokenType;
    public enum TokenType {
        LIMITED,
        UNLIMITED // Безлимитное количество токенов
    }
}
