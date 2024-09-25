package com.example.apipaymentservice.controler;

import com.example.apipaymentservice.dto.ProjectDto;
import com.example.apipaymentservice.maper.ProjectMapper;
import com.example.apipaymentservice.model.Project;
import com.example.apipaymentservice.model.Project.TokenType;
import com.example.apipaymentservice.request.ProjectCreateRequestDto;
import com.example.apipaymentservice.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    //поиск проекта по id юзера
    @GetMapping("/user/{id}")
    public ResponseEntity<List<ProjectDto>> getProjectByUserId(@PathVariable Long id) {
        List<ProjectDto> projectDto = projectService.getProjectsByUserId(id); // Получаем проект по userId
        return ResponseEntity.ok(projectDto); // Возвращаем ответ
    }

    //поиск проекта по id
    @GetMapping("project/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        ProjectDto projectDto = projectService.getProjectById(id);
        return ResponseEntity.ok(projectDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        List<ProjectDto> projectDtoList = projectService.projectList();
        return ResponseEntity.ok(projectDtoList);
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectCreateRequestDto projectRequest) {
        // Преобразуем строковый параметр в enum
        TokenType type = TokenType.valueOf(projectRequest.getTokenType().toUpperCase());

        // Создаем проект
        ProjectDto createdProject = projectService.createProject(
                projectRequest.getTitle(),
                projectRequest.getDescription(),
                type,
                projectRequest.getUserId()
        );

        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    //поиск по ключу
    @GetMapping("/{apiKey}")
    public ResponseEntity<ProjectDto> getProjectByApiKey(@PathVariable String apiKey) {
        Project project = projectService.getProjectByApiKey(apiKey);
        ProjectDto projectDto = projectMapper.convertToDTO(project);
        return ResponseEntity.ok(projectDto);
    }

    //использтвать токен
    @PostMapping("/{apiKey}/use-token")
    public ResponseEntity<String> useToken(@PathVariable String apiKey) {
        Project project = projectService.getProjectByApiKey(apiKey);
        boolean tokenUsed = projectService.useToken(project);
        if (tokenUsed) {
            return ResponseEntity.ok("Токен успешно использован");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Недостаточно токенов");
        }
    }
}
