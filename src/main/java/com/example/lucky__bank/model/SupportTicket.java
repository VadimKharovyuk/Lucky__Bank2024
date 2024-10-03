package com.example.lucky__bank.model;

import com.example.lucky__bank.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "support_tickets")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private String topic;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "admin_reply")
    private String adminReply;

    @Column(name = "replied_at")
    private LocalDateTime repliedAt;



    public SupportTicket(Long userId, String message, String topic) {
        this.userId = userId;
        this.message = message;
        this.topic = topic;
        this.createdAt = LocalDateTime.now();
    }
}
