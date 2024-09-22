package com.example.lucky__bank.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;
}
