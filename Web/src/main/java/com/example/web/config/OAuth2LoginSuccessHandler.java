package com.example.web.config;

import com.example.web.dto.UserDTO;
import com.example.web.repository.UserFeignClient;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserFeignClient userFeignClient;

    public OAuth2LoginSuccessHandler(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String username = oauthUser.getAttribute("name"); // Убедитесь, что это правильное поле

        // Проверка наличия пользователя в базе данных
        if (!userExists(email)) {
            // Создание нового пользователя
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(email);
            userDTO.setUsername(username); // Используйте правильное поле
            userDTO.setRole("USER");
            userDTO.setBlocked(false);

            // Регистрация пользователя через FeignClient
            userFeignClient.registerUser(userDTO);
        }
        log.info("OAuth2 User Email: {}", email);
        log.info("OAuth2 User Name: {}", username);

        response.sendRedirect("/");
    }





    private boolean userExists(String email) {
        try {
            // Попытка получить пользователя по email через FeignClient
            UserDTO existingUser = userFeignClient.getUserByEmail(email);
            return existingUser != null;
        } catch (FeignException e) {

            return false;
        }
    }
}