package com.example.web.dto;

import lombok.Data;

@Data
public class FAQDto {
    private Long id;
    private String topic;
    private String question;
    private String answer;
    private boolean isActive;
}
