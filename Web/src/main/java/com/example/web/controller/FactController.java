package com.example.web.controller;

import com.example.web.dto.FactDto;
import com.example.web.service.FactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/facts")
public class FactController {
    private final FactService factService;



    @GetMapping
    public String showFact(Model model) {
        // Добавляем доступные типы фактов в модель
        model.addAttribute("factTypes", FactDto.FactType.values());
        return "user/facts/select"; // имя HTML файла, который будет отображать выбор типа фактов
    }

    @GetMapping("/random")
    public ResponseEntity<FactDto> getRandomFact(@RequestParam("type") FactDto.FactType type) {
        FactDto randomFact = factService.random(type);
        return ResponseEntity.ok(randomFact);
    }


}
