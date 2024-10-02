package com.example.lucky__bank.dto;

import com.example.lucky__bank.model.Fact;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FactDto {

    private Long id;
    private String content;

    private Fact.FactType type;


}
