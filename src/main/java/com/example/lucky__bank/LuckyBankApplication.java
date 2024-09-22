package com.example.lucky__bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LuckyBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyBankApplication.class, args);
    }

}
