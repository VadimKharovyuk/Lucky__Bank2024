package com.example.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
}
