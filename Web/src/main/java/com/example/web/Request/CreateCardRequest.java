package com.example.web.Request;

import lombok.Data;

@Data
public class CreateCardRequest {
    private Long userId;
    private String cardType;
}
