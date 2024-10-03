package com.example.lucky__bank.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FAQDto {
    private Long id;
    private String topic;
    private String question;
    private String answer;
    private boolean isActive ;

}
