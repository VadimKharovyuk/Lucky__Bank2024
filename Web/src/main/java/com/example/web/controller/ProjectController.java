package com.example.web.controller;

import com.example.web.Request.ProjectCreateRequest;
import com.example.web.dto.ProjectDto;
import com.example.web.dto.UserDTO;
import com.example.web.service.ApiPaymentService;
import com.example.web.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ApiPaymentService apiPaymentService;
    private final UserService userService ;


    @GetMapping("/projects/new")
    public String showCreateProjectForm(Model model) {

        model.addAttribute("projectCreateRequest", new ProjectCreateRequest());

        // Получаем текущего аутентифицированного пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = null; // Инициализация переменной

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            user = userService.findByEmail(email);
        } else {
            String username = authentication.getName();
            user = userService.findByUsername(username);
        }

        // Проверьте, что объект user не null
        if (user != null) {
            model.addAttribute("user", user); // Добавляем пользователя в модель
        } else {
            model.addAttribute("error", "User not found");
        }

        return "user/projects/createProject"; // Имя HTML-шаблона

    }

    // Обработка создания проекта
    @PostMapping("/projects")
    public String createProject(ProjectCreateRequest projectCreateRequest, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = null; // Инициализация переменной

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            user = userService.findByEmail(email);
        } else {
            String username = authentication.getName();
            user = userService.findByUsername(username);
        }

        // Проверьте, что объект user не null
        if (user != null) {
            projectCreateRequest.setUserId(user.getId()); // Устанавливаем userId в объект запроса
            ProjectDto createdProject = apiPaymentService.createProject(projectCreateRequest);

            // Перенаправляем на страницу деталей проекта
            return "redirect:/projects/" + createdProject.getApiKey();
        } else {
            model.addAttribute("error", "User not found");
            return "user/projects/createProject"; // Вернуть на страницу создания проекта с ошибкой
        }
    }


    // Метод для отображения деталей проекта по API-ключу
    @GetMapping("/projects/{apiKey}")
    public String getProjectDetails(@PathVariable String apiKey, Model model) {
        ProjectDto project = apiPaymentService.getProjectByApiKey(apiKey);
        if (project != null) {
            model.addAttribute("project", project);
            return "user/projects/projectDetails";
        } else {
            model.addAttribute("error", "Проект не найден с указанным API-ключом.");
            return "user/projects/error";
        }
    }


    @GetMapping("/user/projects")
    public String getUserProjects(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;

        // Получение userId в зависимости от типа аутентификации
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
            String email = oauthUser.getAttribute("email");
            UserDTO user = userService.findByEmail(email);
            userId = user.getId();
        } else {
            String username = authentication.getName();
            UserDTO user = userService.findByUsername(username);
            userId = user.getId();
        }

        // Получение проектов пользователя

        List<ProjectDto> userProjects = apiPaymentService.getProjectsByUserId(userId);
        model.addAttribute("projects", userProjects);

        return "user/projects/userProjects";
    }

    @GetMapping("/api/document")
    public String document (){
        return "user/projects/API Documentation Page";
    }
    @PostMapping("/projects/delete/{id}")
    public String deleteByIdProject(@PathVariable Long id) {
        apiPaymentService.deleteProjectById(id);
        return "redirect:/user/projects";
    }
}
