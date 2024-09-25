package com.example.apipaymentservice.dto;

import com.example.apipaymentservice.model.Project;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Builder
public class ProjectDto {
    private Long id;


    private String title;
    private String description;
    private String apiKey;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate lastResetDate;
    private Integer tokens; // Количество токенов
    private Project.TokenType tokenType; // Тип токенов: лимитированные или безлимитные
    private Long userId;
}
