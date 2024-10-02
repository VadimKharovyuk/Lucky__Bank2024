package com.example.web.service;

import com.example.web.dto.FactDto;
import com.example.web.repository.FactServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FactService {
    private final FactServiceClient client ;



    // Метод для получения факта через FeignClient
    public FactDto random(FactDto.FactType factType) {
        return client.getRandomFact(factType);
    }
}
