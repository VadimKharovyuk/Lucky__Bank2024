package com.example.web.service;

import com.example.web.Request.ReplenishCardRequest;
import com.example.web.dto.CardDTO;
import com.example.web.dto.ReplenishmentDto;
import com.example.web.repository.ReplenishmentServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
public class ReplenishmentService {
    private  final ReplenishmentServiceClient client ;
     private final CardService cardService ;



    public String replenishCard(ReplenishCardRequest cardRequest) {
        // Выполняем запрос через Feign-клиент и извлекаем тело ответа
        ResponseEntity<String> response = client.replenishCard(cardRequest);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Ошибка при пополнении карты: " + response.getStatusCode());
        }
    }




    public List<ReplenishmentDto> getReplenishmentHistory(Long cardId) {
        // Получаем карту по ID
        CardDTO card = cardService.findByIdCard(cardId);

        // Проверяем, была ли карта найдена
        if (card == null) {
            throw new IllegalArgumentException("Card not found with id: " + cardId);
        }

        // Получаем историю пополнений, используя ID карты
        return client.getReplenishmentHistory(card.getId());
    }
}

