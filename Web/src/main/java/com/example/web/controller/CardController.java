package com.example.web.controller;

import com.example.web.dto.CardDTO;
import com.example.web.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/create")
    public String showCreateCardForm(Model model) {
        // Добавляем возможные типы карт в модель
        model.addAttribute("cardTypes", CardDTO.CardType.values());
        model.addAttribute("card", new CardDTO());
        return "user/card/Create";
    }

    @PostMapping("/create")
    public String createCard(@RequestParam Long userId,
                             @RequestParam String cardType,
                             RedirectAttributes redirectAttributes) {
        try {
            CardDTO cardDTO = cardService.createCard(userId, cardType);
            redirectAttributes.addFlashAttribute("message", "Card created successfully: " + cardDTO.getCardNumber());
            return "redirect:/cards/create"; // Перенаправление после успешного создания
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating card: " + e.getMessage());
            return "redirect:/cards/create"; // Перенаправление в случае ошибки
        }
    }
}