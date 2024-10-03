package com.example.web.Request;

import lombok.Data;

@Data
public class FAQRequest {
    private Long id;
    private String topic;
    private String question;
    private String answer;
    private boolean isActive = true;
}
