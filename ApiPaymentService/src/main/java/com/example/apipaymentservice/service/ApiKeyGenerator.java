package com.example.apipaymentservice.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class ApiKeyGenerator {
    public static String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
