package com.example.web.controller;

import com.example.web.Request.ProfileRequest;
import com.example.web.dto.ProfileDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.ProfileService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping("/profiles/new")
    public String showProfileForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = null; // Инициализация переменной

        // Получаем информацию о текущем пользователе
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            // Поищите пользователя в базе данных по email
            user = userService.findByEmail(email);
        } else {
            String username = authentication.getName();
            user = userService.findByUsername(username); // Получаем пользователя из БД
        }

        // Проверьте, что объект user не null
        if (user != null) {
            model.addAttribute("user", user); // Добавьте пользователя в модель
            model.addAttribute("profileRequest", new ProfileRequest());
            model.addAttribute("Gender", ProfileDTO.Gender.values());
            model.addAttribute("MaritalStatus", ProfileDTO.MaritalStatus.values());
            model.addAttribute("citizenship", ProfileDTO.Citizenship.values());
            return "/user/profile/profileForm"; // Вернемся на страницу формы профиля
        } else {
            // Добавьте обработку случая, когда пользователь не найден
            model.addAttribute("error", "User not found");
            return "redirect:/login"; // Переход на страницу входа или другую страницу
        }
    }

    @PostMapping("/profiles")
    public String createProfile(ProfileRequest profileRequest, Model model) {
        ProfileDTO createdProfile = profileService.crateProfile(profileRequest); // Исправлено на createProfile
        model.addAttribute("profile", createdProfile);
        return "/user/profile/profileSuccess"; // имя HTML-шаблона для успешного создания профиля
    }

@GetMapping("/view")
public String viewProfile(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDTO user = null; // Инициализация переменной

    // Получаем информацию о текущем пользователе
    if (authentication.getPrincipal() instanceof OAuth2User) {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");

        // Поищите пользователя в базе данных по email
        user = userService.findByEmail(email);
    } else {
        String username = authentication.getName();
        user = userService.findByUsername(username); // Получаем пользователя из БД
    }

    // Проверяем, что объект user не null
    if (user != null) {
        Long userId = user.getId(); // Получаем ID пользователя
        ProfileDTO profile = profileService.getProfileByUserId(userId); // Получаем профиль пользователя по ID
        model.addAttribute("profile", profile); // Добавляем профиль в модель

        // Важно добавить проверку на наличие профиля
        if (profile == null) {
            model.addAttribute("profile", null); // Устанавливаем профиль как null, если не найден
        }

        return "/user/profile/profileView"; // Путь к HTML-шаблону для просмотра профиля
    }

    return "redirect:/login"; // Переход на страницу входа, если пользователь не найден
}
}