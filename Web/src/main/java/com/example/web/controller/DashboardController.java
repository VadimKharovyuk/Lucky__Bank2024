package com.example.web.controller;

import com.example.web.dto.CardDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.CardService;
import com.example.web.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final CardService cardService;
    private final UserService userService;



    @GetMapping("/{userId}")
    public String showDashboard(@PathVariable Long userId, Model model) {
        UserDTO user = userService.findById(userId); // Получаем данные пользователя по ID
        List<CardDTO> cards = cardService.getCardsByUserId(userId); // Получаем все карты пользователя

        model.addAttribute("user", user);
        model.addAttribute("cards", cards);

        return "user/dashbord/test";
    }

    // Создание новой карты
    @PostMapping("/create-card")
    public String createCard(@RequestParam Long userId, @RequestParam String cardType, Model model) {
        cardService.createCard(userId, cardType); // Создаем новую карту

        return "redirect:/dashboard/" + userId; // Перенаправляем пользователя обратно на dashboard
    }
}