package com.example.web.controller;

import com.example.web.Request.LoginRequest;
import com.example.web.dto.UserDTO;
import com.example.web.Request.UserRegistrationRequest;
import com.example.web.repository.UserFeignClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserFeignClient userFeignClient;
    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginRequest loginRequest, RedirectAttributes redirectAttributes) {
        log.info("Attempting to log in user: {}", loginRequest.getUsername());

        try {
            // Вызов Feign-клиента для выполнения запроса на логин
            UserDTO userDTO = userFeignClient.login(loginRequest);

            if (userDTO == null) {
                log.warn("UserDTO is null after login attempt.");
                redirectAttributes.addFlashAttribute("error", "Неправильный логин или пароль");
                return "redirect:/login";
            }

            log.info("User logged in: {}", userDTO.getUsername());

            // Проверка, если пользователь заблокирован
            if (userDTO.isBlocked()) {
                log.warn("User {} is blocked.", userDTO.getUsername());
                return "redirect:/blocked";
            }

            // Создание аутентификационного токена
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDTO.getUsername(),
                    null, // Пароль не нужен, так как аутентификация уже выполнена
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userDTO.getRole()))
            );

            // Установка аутентификации в контексте безопасности
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Успешный вход, перенаправление на главную страницу
            return "redirect:/";
        } catch (FeignException e) {
            log.error("Login failed: {}", e.getMessage(), e);
            // Сообщение об ошибке, если логин не удался
            redirectAttributes.addFlashAttribute("error", "Неправильный логин или пароль");
            return "redirect:/login";
        }
    }

    @GetMapping("/user/{username}")
    public String testGetUserByUsername(@PathVariable String username, Model model) {
        try {
            // Использование FeignClient для получения информации о пользователе
            UserDTO userDTO = userFeignClient.getUserByUsername(username);

            if (userDTO == null) {
                log.warn("User not found for username: {}", username);
                model.addAttribute("error", "User not found");
                return "error"; // Назначьте соответствующую страницу ошибки
            }

            // Добавление пользователя в модель для отображения на странице
            model.addAttribute("user", userDTO);
            return "user/userDetails"; // Назначьте страницу, где будет отображаться информация о пользователе
        } catch (Exception e) {
            log.error("Error occurred while fetching user details", e);
            model.addAttribute("error", "An error occurred");
            return "error"; // Назначьте соответствующую страницу ошибки
        }
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegistrationRequest userDTO, Model model) {
        try {
            userFeignClient.registerUser(userDTO);
            return "redirect:/login";
        } catch (FeignException e) {
            // Обработка ошибок от Feign клиента
            if (e.status() == 409) { // Если сервер вернул 409 (Email already exists)
                model.addAttribute("error", "Этот email уже занят.");
            } else {
                model.addAttribute("error", "Ошибка регистрации. Попробуйте снова.");
            }
            model.addAttribute("userDto", userDTO); // Добавляем объект в модель для повторного отображения
            return "/user/user/register";
        }
    }


    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("userDto", new UserDTO());
        return "user/user/Login";
    }

    @GetMapping("/blocked")
    public String blocked(){
        return "user/Blocked";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userDto", new UserDTO());
        return "user/user/register";
    }



    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<UserDTO> users = userFeignClient.getAllUsers();
        model.addAttribute("users", users);
        return "user/users"; // Имя HTML-шаблона для отображения списка пользователей
    }


    @PostMapping("/users/block/{userId}")
    public String blockUser(@PathVariable Long userId) {
        userFeignClient.blockUser(userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/unblock/{userId}")
    public String unblockUser(@PathVariable Long userId) {
        userFeignClient.unblockUser(userId);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/is-blocked")
    public String isUserBlocked(@RequestParam String username, Model model) {
        Boolean isBlocked = userFeignClient.isBlocked(username);
        model.addAttribute("isBlocked", isBlocked);
        return "user-status"; // Имя HTML-шаблона для отображения статуса пользователя
    }
    @PostMapping("/users/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userFeignClient.deleteUserById(id);
        return "redirect:/admin/users";

    }


}
