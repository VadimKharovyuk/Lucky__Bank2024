package com.example.web.controller;

import com.example.web.Request.ProfileRequest;
import com.example.web.dto.ProfileDTO;
import com.example.web.dto.UserDTO;
import com.example.web.service.ProfileService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Base64;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
@Slf4j
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping("/upload-photo")
    public String showUploadPhotoPage(Model model) {
        UserDTO currentUser = getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
        model.addAttribute("user", currentUser);
        return "user/profile/upload-photo";
    }

    @PostMapping("/upload-photo")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        UserDTO currentUser = getCurrentUser();
        try {
            String result = profileService.uploadPhoto(currentUser.getId(), file);
            log.info("Photo upload result for user {}: {}", currentUser.getId(), result);
            return "redirect:/dashboard";
        } catch (Exception e) {
            log.error("Error uploading photo for user {}: {}", currentUser.getId(), e.getMessage());
            return "redirect:/profile/upload-photo?error";
        }
    }

    @GetMapping("/{userId}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long userId) {
        log.info("Requesting photo for user: {}", userId);
        byte[] photo = profileService.getProfilePhoto(userId);
        if (photo != null && photo.length > 0) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(photo);
        } else {
            log.warn("No photo found for user: {}", userId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/profiles/{userId}/edit")
    public String showUpdateProfileForm(@PathVariable Long userId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = null; // Инициализация переменной

        // Получаем информацию о текущем пользователе
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            user = userService.findByEmail(email); // Поиск пользователя по email
        } else {
            String username = authentication.getName();
            user = userService.findByUsername(username); // Получаем пользователя из БД
        }

        // Проверяем, что объект user не null
        if (user != null) {
            Long currentUserId = user.getId(); // Получаем ID текущего пользователя

            // Получаем профиль пользователя по его ID
            ProfileDTO profileDTO = profileService.getProfileByUserId(currentUserId);

            // Создаем объект ProfileRequest
            ProfileRequest profileRequest = new ProfileRequest();
            if (profileDTO != null) {
                // Заполняем profileRequest данными из профиля
                profileRequest.setUserId(currentUserId); // Устанавливаем userId
                profileRequest.setFullName(profileDTO.getFullName());
                profileRequest.setPhoneNumber(profileDTO.getPhoneNumber());
                profileRequest.setAddress(profileDTO.getAddress());
                profileRequest.setDateOfBirth(LocalDate.parse(profileDTO.getDateOfBirth()));
                profileRequest.setPassportNumber(profileDTO.getPassportNumber());
                profileRequest.setEmploymentWorkPlace(profileDTO.getEmploymentWorkPlace());
                profileRequest.setGender(profileDTO.getGender());
                profileRequest.setMaritalStatus(String.valueOf(profileDTO.getMaritalStatus()));
                profileRequest.setCitizenship(profileDTO.getCitizenship());
            } else {
                // Если профиль не найден, устанавливаем только userId
                profileRequest.setUserId(currentUserId);
            }

            model.addAttribute("profileRequest", profileRequest); // Передаем profileRequest в модель
            model.addAttribute("user", user); // Добавляем пользователя в модель
            model.addAttribute("Gender", ProfileDTO.Gender.values());
            model.addAttribute("MaritalStatus", ProfileDTO.MaritalStatus.values());
            model.addAttribute("citizenship", ProfileDTO.Citizenship.values());

            return "/user/profile/profileForm"; // Возвращаем имя HTML-шаблона
        } else {
            return "redirect:/error"; // Замените на нужный вам маршрут
        }
    }



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
        byte[] photo = profileService.getProfilePhoto(user.getId());

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


    private UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            return userService.findByEmail(email);
        } else {
            String username = authentication.getName();
            return userService.findByUsername(username);
        }
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
        model.addAttribute("user", user); // Добавьте это, если еще не добавлено

        // Важно добавить проверку на наличие профиля
        if (profile == null) {
            model.addAttribute("profile", null); // Устанавливаем профиль как null, если не найден
        }

        return "/user/profile/profileView"; // Путь к HTML-шаблону для просмотра профиля
    }

    return "redirect:/login"; // Переход на страницу входа, если пользователь не найден
}


    // Метод для обработки обновления профиля
    @PostMapping("/profiles/update")
    public String updateProfile(@ModelAttribute ProfileRequest profileRequest) {
        profileService.update(profileRequest.getUserId(), profileRequest);
        return "redirect:/profile/view";
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