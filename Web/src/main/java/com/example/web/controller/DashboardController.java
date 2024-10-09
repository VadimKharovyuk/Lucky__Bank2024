package com.example.web.controller;

import com.example.web.dto.CardDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final ProfileService profileService;
    private final UserService userService;
    private final CurrencyService currencyService;


    @GetMapping
    public String showAccount(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = getUserFromAuthentication(authentication);

        List<CurrencyRate> rates = currencyService.getCurrencyRates();
        model.addAttribute("rates", rates);
        model.addAttribute("currencies", Arrays.asList("UAH", "USD", "EUR"));

        if (user != null) {
            model.addAttribute("user", user);
            addProfilePhotoToModel(model, user.getId());
            addBirthdayMessageToModel(model, user.getId());
        } else {
            model.addAttribute("error", "User not found");
            return "error";
        }

        return "user/dashbord/Personal Bank Account";
    }

    private UserDTO getUserFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            return userService.findByEmail(email);
        } else {
            String username = authentication.getName();
            return userService.findByUsername(username);
        }
    }

    private void addProfilePhotoToModel(Model model, Long userId) {
        byte[] photo = profileService.getProfilePhoto(userId);
        if (photo != null) {
            String base64Photo = Base64.getEncoder().encodeToString(photo);
            model.addAttribute("profilePhoto", base64Photo);
        }
    }

    private void addBirthdayMessageToModel(Model model, Long userId) {
        Optional<String> birthdayMessage = profileService.checkBirthday(userId);
        birthdayMessage.ifPresent(message -> model.addAttribute("birthdayMessage", message));

    }
}


