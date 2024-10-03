package com.example.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupportTicketDto {
    private Long id;

    private Long userId;
    private String message;
    private String topic;
    private LocalDateTime createdAt;
    private String adminReply;
    private LocalDateTime repliedAt;
    private String userName;
}
