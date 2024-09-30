package com.example.web.service;

import com.example.web.Request.CreditRequestDto;
import com.example.web.dto.CreditDto;
import com.example.web.repository.CreditServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.math.BigDecimal;
import java.util.List;

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

    public List<CreditDto> getCreditsByUserAndCard(Long userId, Long cardId) {
        return creditServiceClient.getCreditsByUserAndCard(userId, cardId);
    }


}
