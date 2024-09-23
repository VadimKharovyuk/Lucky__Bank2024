package com.example.web.controller;

import com.example.web.dto.CardDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.CardService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final UserService userService;



    @GetMapping("/{userId}")
    public String showDashboard(@PathVariable Long userId, Model model) {
        UserDTO user = userService.findById(userId);
        List<CardDTO> cards = cardService.getCardsByUserId(userId);

        model.addAttribute("user", user);
        model.addAttribute("cards", cards);

        return "/user/card/CardClientList";
    }

    @GetMapping("/all")
    public String getAllCards(Model model){
       List<CardDTO> cardDTO = cardService.getAllCards();
       model.addAttribute("listCard",cardDTO);
       return "listCard";
    }

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
        System.out.println("user  id = " + userId);
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