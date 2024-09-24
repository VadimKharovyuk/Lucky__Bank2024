package com.example.web.service;

import com.example.web.Request.MoneyTransferRequest;
import com.example.web.dto.TransactionDto;
import com.example.web.repository.MoneyTransferClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoneyTransferService {
    private final MoneyTransferClient moneyTransferClient;

    public TransactionDto sendMoney(MoneyTransferRequest moneyTransferRequest) {
        return moneyTransferClient.transferMoney(moneyTransferRequest);
    }
}
