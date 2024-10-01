package com.example.web.service;

import com.example.web.dto.CardDTO;
import com.example.web.repository.BonusServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BonusService {
    private final BonusServiceClient bonusServiceClient;

    public Map<String, String> claimDailyBonus(Long cardId) {
        ResponseEntity<Map<String, String>> response = bonusServiceClient.claimDailyBonus(cardId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Ошибка при начислении бонуса: " + response.getStatusCode());
        }
    }


}
