package com.example.web.service;

import com.example.web.dto.CardDTO;
import com.example.web.dto.DepositDto;
import com.example.web.dto.UserDTO;
import com.example.web.repository.DepositServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositService {
    private final DepositServiceClient depositServiceClient;


    public DepositDto create(Long userId, Long cardId, BigDecimal amount) {
        return depositServiceClient.create(userId, cardId, amount);
    }

    public List<DepositDto> findDepositsByUserAndCard(Long userId, Long cardId) {
        return depositServiceClient.findDepositsByUserAndCard(userId, cardId);
    }

    public DepositDto withdrawAllFromDeposit(Long userId, Long cardId) {
        return depositServiceClient.withdrawAllFromDeposit(userId, cardId);
    }


}



