package com.example.lucky__bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class LuckyBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyBankApplication.class, args);
    }








//-админка

    //вывести сумму всех карточек клиента


//-сброс пароля через почту
// -изменить пароль в лк

//- конвектор для $ карты с UAH


//- история транзакций
// -api валют


}
