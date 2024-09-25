package com.example.web.controller;

import com.example.web.dto.CardDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.CardService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cards")
@RequiredArgsConstructor
@Slf4j
public class CardController {

    private final CardService cardService;
    private final UserService userService;


    @PostMapping("delete/{id}")
    public String deleteCardById(@PathVariable Long id){
        cardService.deleteCardById(id);
        return "redirect:/cards";
    }
    @GetMapping
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDTO user = null; // Инициализация переменной

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String username = oauthUser.getAttribute("name");
            String email = oauthUser.getAttribute("email");

            // Поищите пользователя в базе данных по email или username
            user = userService.findByEmail(email); // Убедитесь, что у вас есть метод для поиска по email
        } else {
            String username = authentication.getName();
            user = userService.findByUsername(username); // Получаем пользователя из БД
        }
        List<CardDTO> cardUsers = cardService.getCardsByUserId(user.getId());
        model.addAttribute("cards", cardUsers);


        // Проверьте, что объект user не null
        if (user != null) {
            model.addAttribute("user", user); // Добавьте пользователя в модель
        } else {
            // Обработка случая, когда пользователь не найден
            model.addAttribute("error", "User not found");
        }

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDTO user = null; // Инициализация переменной

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String username = oauthUser.getAttribute("name");
            String email = oauthUser.getAttribute("email");

            // Поищите пользователя в базе данных по email или username
            user = userService.findByEmail(email); // Убедитесь, что у вас есть метод для поиска по email
        } else {
            String username = authentication.getName();
            user = userService.findByUsername(username); // Получаем пользователя из БД
        }
        List<CardDTO> cardUsers = cardService.getCardsByUserId(user.getId());
        model.addAttribute("cards", cardUsers);


        // Проверьте, что объект user не null
        if (user != null) {
            model.addAttribute("user", user); // Добавьте пользователя в модель
        } else {
            // Обработка случая, когда пользователь не найден
            model.addAttribute("error", "User not found");
        }
        // Добавляем возможные типы карт в модель
        model.addAttribute("cardTypes", CardDTO.CardType.values());
        model.addAttribute("card", new CardDTO());
        return "user/card/Create";
    }

    @PostMapping("/create")
    public String createCard(@RequestParam Long userId,
                             @RequestParam String cardType,
                             RedirectAttributes redirectAttributes) {
        System.out.println("Received userId: " + userId);
        System.out.println("Received cardType: " + cardType);
        try {
            CardDTO cardDTO = cardService.createCard(userId, cardType);
            redirectAttributes.addFlashAttribute("message", "Card created successfully: " + cardDTO.getCardNumber());
            return "redirect:/cards"; // Перенаправление на страницу со списком карт после создания
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating card: " + e.getMessage());
            return "redirect:/cards/create"; // Перенаправление в случае ошибки
        }
    }


}