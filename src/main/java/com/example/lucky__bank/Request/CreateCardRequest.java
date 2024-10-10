package com.example.lucky__bank.Request;

import com.example.lucky__bank.model.Card;
import lombok.Data;

@Data
public class CreateCardRequest {
    private Long userId;
    private String cardType;

}
