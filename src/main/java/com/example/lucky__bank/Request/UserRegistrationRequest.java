package com.example.lucky__bank.Request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
}
