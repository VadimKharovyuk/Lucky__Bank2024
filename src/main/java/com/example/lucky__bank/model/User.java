package com.example.lucky__bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "blocked")
    private boolean blocked = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum Role {
        USER, ADMIN
    }
}
