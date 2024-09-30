package com.example.web.controller;

import com.example.web.dto.CardDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.CardService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
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
    public String deleteCardById(@PathVariable Long id) {
        cardService.deleteCardById(id);
        return "redirect:/cards";
    }

    @GetMapping
    public String showDashboard(Model model) {
        UserDTO user = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        if (user == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }

        List<CardDTO> cardUsers = cardService.getCardsByUserId(user.getId());
        model.addAttribute("cards", cardUsers);
        model.addAttribute("user", user);

        return "/user/card/CardClientList";
    }

    @GetMapping("/all")
    public String getAllCards(Model model) {
        List<CardDTO> cardDTO = cardService.getAllCards();
        model.addAttribute("listCard", cardDTO);
        return "listCard";
    }

    @GetMapping("/create")
    public String showCreateCardForm(Model model) {
        UserDTO user = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        if (user == null) {
            model.addAttribute("error", "User not found");
            return "redirect:/login";
        }

        List<CardDTO> cardUsers = cardService.getCardsByUserId(user.getId());
        model.addAttribute("cards", cardUsers);
        model.addAttribute("user", user);
        model.addAttribute("cardTypes", CardDTO.CardType.values());
        model.addAttribute("card", new CardDTO());

        return "user/card/Create";
    }

    @PostMapping("/create")
    public String createCard(@RequestParam Long userId,
                             @RequestParam String cardType,
                             RedirectAttributes redirectAttributes) {
        log.info("Received userId: {} and cardType: {}", userId, cardType);

        try {
            // Проверяем, что текущий пользователь создает карту для себя
            UserDTO currentUser = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
            if (currentUser == null || !currentUser.getId().equals(userId)) {
                redirectAttributes.addFlashAttribute("error", "Unauthorized card creation attempt");
                return "redirect:/cards";
            }

            CardDTO cardDTO = cardService.createCard(userId, cardType);
            redirectAttributes.addFlashAttribute("message",
                    String.format("Card created successfully: %s", cardDTO.getCardNumber()));
            return "redirect:/cards";
        } catch (Exception e) {
            log.error("Error creating card", e);
            redirectAttributes.addFlashAttribute("error",
                    String.format("Error creating card: %s", e.getMessage()));
            return "redirect:/cards/create";
        }
    }

    private UserDTO getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            return userService.findByEmail(email);
        } else {
            String username = authentication.getName();
            return userService.findByUsername(username);
        }
    }
}

