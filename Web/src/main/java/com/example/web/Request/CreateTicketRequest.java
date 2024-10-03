package com.example.web.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateTicketRequest {

    private Long userId;
    private String topic;
    private String message;


}
