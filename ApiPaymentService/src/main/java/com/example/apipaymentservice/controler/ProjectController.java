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


@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody ProjectCreateRequestDto projectRequest) {
        // Преобразуем строковый параметр в enum
        TokenType type = TokenType.valueOf(projectRequest.getTokenType().toUpperCase());

        // Создаем проект
        ProjectDto createdProject = projectService.createProject(
                projectRequest.getTitle(),
                projectRequest.getDescription(),
                type
        );

        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

//    @PostMapping
//    public ResponseEntity<ProjectDto> createProject(@RequestParam String title,
//                                                    @RequestParam String description,
//                                                    @RequestParam String tokenType) {
//        // Преобразуем строковый параметр в enum
//        TokenType type = TokenType.valueOf(tokenType.toUpperCase());
//        ProjectDto createdProject = projectService.createProject(title, description, type);
//        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
//    }

    @GetMapping("/{apiKey}")
    public ResponseEntity<ProjectDto> getProjectByApiKey(@PathVariable String apiKey) {
        Project project = projectService.getProjectByApiKey(apiKey);
        ProjectDto projectDto = projectMapper.convertToDTO(project);
        return ResponseEntity.ok(projectDto);
    }

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