package com.example.lucky__bank.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean blocked;
    private LocalDateTime createdAt;



}
