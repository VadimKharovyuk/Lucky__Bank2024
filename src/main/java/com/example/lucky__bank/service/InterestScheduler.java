package com.example.lucky__bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestScheduler {

    private final DepositService depositService;

    // Ежедневно в полночь начисляются проценты на депозиты
//    @Scheduled(cron = "0 0 0 * * ?")  // Шедулер на каждый день в 00:00
//    public void scheduleDailyInterest() {
//        depositService.applyDailyInterest();
//    }

    @Scheduled(cron = "0 * * * * ?")  // Шедулер для запуска каждую минуту
    public void scheduleDailyInterest() {
        depositService.applyDailyInterest();
    }
}