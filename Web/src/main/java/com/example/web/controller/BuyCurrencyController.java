package com.example.web.controller;

import com.example.web.Request.CurrencyPurchaseRequest;
import com.example.web.dto.CardDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.BuyCurrencyService;
import com.example.web.service.CardService;
import com.example.web.service.UserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/buy-currency")
public class BuyCurrencyController {
    private final BuyCurrencyService buyCurrencyService;
    private final UserService userService;
    private final CardService cardService;

    @GetMapping("/form")
    public String buyCurrency(Model model, Authentication authentication) {
        // Получаем текущего пользователя
        UserDTO currentUser = getCurrentUser(authentication);

        if (currentUser != null) {
            model.addAttribute("currentUserId", currentUser.getId());
        }

        // Создаем новый объект CurrencyPurchaseRequest
        CurrencyPurchaseRequest request = new CurrencyPurchaseRequest();

        // Устанавливаем userId из текущего пользователя
        if (currentUser != null) {
            request.setUserId(currentUser.getId());
        }
        List<String> currencies = Arrays.asList("USD", "EUR", "UAH");
        List<CardDTO> list = cardService.getCardsByUserId(request.getUserId());

        model.addAttribute("cards", list);
        model.addAttribute("currencies", currencies);
        model.addAttribute("request", request);

        return "/user/buy-currency/form";
    }

    @PostMapping()
    public String processBuyCurrency(@ModelAttribute CurrencyPurchaseRequest request, Model model) {
        log.info("Обработка покупки валюты для пользователя: {}", request.getUserId());
        try {
            CardDTO updatedCard = buyCurrencyService.buyCurrency(request);
            model.addAttribute("message", "Валюта успешно куплена!");
            model.addAttribute("updatedCard", updatedCard);
        } catch (FeignException e) {
            log.error("Ошибка Feign при обработке покупки валюты: {}", e.getMessage());
            model.addAttribute("error", "Ошибка сервиса: " + e.getMessage());
        } catch (Exception e) {
            log.error("Неожиданная ошибка при обработке покупки валюты: {}", e.getMessage(), e);
            model.addAttribute("error", "Произошла неожиданная ошибка: " + e.getMessage());
        }

        model.addAttribute("request", request);
        return "redirect:/buy-currency/form";
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