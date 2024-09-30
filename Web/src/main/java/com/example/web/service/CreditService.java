package com.example.web.service;

import com.example.web.Request.CreditRequestDto;
import com.example.web.dto.CreditDto;
import com.example.web.repository.CreditServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditServiceClient creditServiceClient;

    public CreditDto create(CreditRequestDto creditRequestDto) {
        return creditServiceClient.create(creditRequestDto);
    }

    public CreditDto approveCredit(Long creditId) {
        return creditServiceClient.approveCredit(creditId);
    }

    public void makePayment(Long creditId, BigDecimal paymentAmount) {
        creditServiceClient.makePayment(creditId, paymentAmount);
    }

    public void deleteCreditById(Long creditId) {
        creditServiceClient.deleteCreditById(creditId);
    }


}
