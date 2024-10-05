package com.example.lucky__bank.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException  extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }

}
