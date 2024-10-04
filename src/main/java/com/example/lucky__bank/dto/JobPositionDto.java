package com.example.lucky__bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class JobPositionDto {

    private Long id;
    private String position ;
}
