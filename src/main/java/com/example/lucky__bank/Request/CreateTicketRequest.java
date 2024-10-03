package com.example.lucky__bank.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketRequest {

    private Long userId;

    @NotBlank(message = "Topic cannot be blank")
    @Size(max = 100, message = "Topic cannot be longer than 100 characters")
    private String topic;

    @NotBlank(message = "Message cannot be blank")
    @Size(max = 500, message = "Message cannot be longer than 500 characters")
    private String message;
}
