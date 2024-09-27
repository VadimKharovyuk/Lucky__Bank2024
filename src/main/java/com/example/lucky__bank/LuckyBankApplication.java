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





////по id ( список транзакий )
////личные данные клиента добавить страницу
////поиск клиента по  .. + udpate клиента + admin (роль)
//
//-добавить номер тлф в модель и приписку
//-админка

//-валидация на логин/пароль

//-сброс пароля через почту
// -изменить пароль в лк
//-google / apple регистрация
// -сапорт с ответом в лк
//-банкомат
// -депозит (добавлять шедулер)
//- конвектор для $ карты с UAH
//-купить шутку
//-купить скидку на шутки
//- история транзакций
// -api валют


}
