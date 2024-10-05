package com.example.lucky__bank.service;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class Base64Utils {

    public byte[] decodeBase64(String base64String) {
        try {
            return Base64.getDecoder().decode(base64String);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Ошибка при декодировании Base64 строки", e);
        }
    }

    public String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
