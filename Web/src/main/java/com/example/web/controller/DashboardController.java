package com.example.web.controller;

import com.example.web.dto.CardDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.CardService;
import com.example.web.service.ProfileService;
import com.example.web.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashboardController {

    private final ProfileService profileService;
    private final UserService userService;

//    @GetMapping
//    public String showAccount(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        UserDTO user = null; // Инициализация переменной
//
//        if (authentication.getPrincipal() instanceof OAuth2User) {
//            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
//            String username = oauthUser.getAttribute("name");
//            String email = oauthUser.getAttribute("email");
//
//
//            // Поищите пользователя в базе данных по email или username
//            user = userService.findByEmail(email);
//        } else {
//            String username = authentication.getName();
//            user = userService.findByUsername(username); // Получаем пользователя из БД
//        }
//
//
//        // Проверьте, что объект user не null
//        if (user != null) {
//            model.addAttribute("user", user);
//        } else {
//            // Обработка случая, когда пользователь не найден
//            model.addAttribute("error", "User not found");
//        }
//
//        return "user/dashbord/Personal Bank Account";
//    }


    @GetMapping
    public String showAccount(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = null;

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            user = userService.findByEmail(email);
        } else {
            String username = authentication.getName();
            user = userService.findByUsername(username);
        }

        if (user != null) {
            model.addAttribute("user", user);

            // Получение фото профиля
            byte[] photo = profileService.getProfilePhoto(user.getId());
            if (photo != null) {
                String base64Photo = Base64.getEncoder().encodeToString(photo);
                model.addAttribute("profilePhoto", base64Photo);
            }
        } else {
            model.addAttribute("error", "User not found");
        }

        return "user/dashbord/Personal Bank Account";
    }
}


