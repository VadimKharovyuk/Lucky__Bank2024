package com.example.web.controller;

import com.example.web.Request.ProjectCreateRequest;
import com.example.web.dto.ProjectDto;
import com.example.web.service.ApiPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ApiPaymentService apiPaymentService;


    @GetMapping("/projects/new")
    public String showCreateProjectForm(Model model) {
        model.addAttribute("projectCreateRequest", new ProjectCreateRequest());
        return "user/projects/createProject"; // Имя HTML-шаблона
    }

    // Обработка создания проекта
    @PostMapping("/projects")
    public String createProject(ProjectCreateRequest projectCreateRequest, Model model) {
        ProjectDto createdProject = apiPaymentService.createProject(projectCreateRequest);
        model.addAttribute("project", createdProject);
        // Добавляем созданный проект в атрибуты для передачи в redirect


        // Перенаправляем на страницу деталей проекта по API-ключу
        return "redirect:/projects/" + createdProject.getApiKey();

    }
    // Метод для отображения деталей проекта по API-ключу
    @GetMapping("/projects/{apiKey}")
    public String getProjectDetails(@PathVariable String apiKey, Model model) {
        ProjectDto project = apiPaymentService.getProjectByApiKey(apiKey);
        if (project != null) {
            model.addAttribute("project", project);
            return "user/projects/projectDetails"; // Имя HTML-шаблона для отображения деталей проекта
        } else {
            model.addAttribute("error", "Проект не найден с указанным API-ключом.");
            return "user/projects/error"; // Имя HTML-шаблона для отображения ошибки (при необходимости)
        }
    }
}
