package com.example.web.controller;

import com.example.web.dto.CardDTO;
import com.example.web.dto.UserDTO;
import com.example.web.repository.BonusServiceClient;
import com.example.web.service.BonusService;
import com.example.web.service.CardService;
import com.example.web.service.UserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/bonus")
@RequiredArgsConstructor
public class BonusController {

    private final BonusService bonusService;
    private final CardService cardService;
    private final UserService userService;

    @GetMapping("/cards")
    public String getUserCards(Model model) {
        UserDTO user = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());

        List<CardDTO> cards = cardService.getCardsByUserId(user.getId());
        model.addAttribute("cards", cards);
        return "user/card/bonusForm";

    }


    @PostMapping("/claim/{cardId}")
    public String claimDailyBonus(@PathVariable Long cardId, RedirectAttributes redirectAttributes) {
        try {
            Map<String, String> response = bonusService.claimDailyBonus(cardId);
            redirectAttributes.addFlashAttribute("successMessage", "Бонус успешно начислен на карту! Сумма: " + response.get("amount"));
        } catch (FeignException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось начислить бонус: " + e.getMessage());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cards";
    }







    private  UserDTO getCurrentUser(Authentication authentication) {
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