package com.example.web.exeption;

public class CustomServiceException extends RuntimeException   {
    public CustomServiceException(String message, Exception e) {
        super(message);
    }
}
