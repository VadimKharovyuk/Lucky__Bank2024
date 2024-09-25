package com.example.apipaymentservice.service;

import com.example.apipaymentservice.dto.ProjectDto;
import com.example.apipaymentservice.maper.ProjectMapper;
import com.example.apipaymentservice.model.Project;

import com.example.apipaymentservice.repository.ProjectRepository;
import com.example.apipaymentservice.request.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.BitSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;



    // Метод для создания проекта
    public ProjectDto createProject(String title, String description, Project.TokenType tokenType,Long userId) {
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setApiKey(generateApiKey());
        project.setTokenType(tokenType);
        project.setUserId(userId);

        // Устанавливаем количество токенов в зависимости от типа
        if (tokenType == Project.TokenType.LIMITED) {
            project.setTokens(100); // Устанавливаем 100 токенов для лимитированного типа
        } else {
            project.setTokens(null); // Для безлимитного типа не требуется количество токенов
        }

        // Сохраняем проект в базу данных
        Project savedProject = projectRepository.save(project);

        // Преобразуем сущность Project в DTO и возвращаем
        return projectMapper.convertToDTO(savedProject);
    }

    // Генерация API-ключа
    public String generateApiKey() {
        return UUID.randomUUID().toString();
    }

    public boolean useToken(Project project) {
        // Сброс токенов, если день изменился
        LocalDate today = LocalDate.now();
        if (project.getLastResetDate() == null || !project.getLastResetDate().isEqual(today)) {
            project.setTokens(100); // Или установите нужное значение для сброса токенов
            project.setLastResetDate(today); // Обновляем дату сброса
            projectRepository.save(project); // Сохраняем обновленный проект
        }

        if (project.getTokenType() == Project.TokenType.LIMITED) {
            if (project.getTokens() != null && project.getTokens() > 0) {
                project.setTokens(project.getTokens() - 1);
                projectRepository.save(project); // Обновляем проект с уменьшенным числом токенов
                return true; // Успешно использован токен
            } else {
                return false; // Недостаточно токенов
            }
        }
        return true; // Для безлимитного количества токенов всегда true
    }

    public List<ProjectDto> projectList (){
        List<Project> project = projectRepository.findAll();
        return  project.stream()
                .map(projectMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDto getProjectById(Long id){
        Project project = projectRepository.getProjectsById(id)
                .orElseThrow(()-> new RuntimeException("Проект с таким id не найден" + id));
        return projectMapper.convertToDTO(project);
    }

    public Project getProjectByApiKey(String apiKey) {
        return projectRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Проект с таким API-ключом не найден"));
    }

    public List<ProjectDto> getProjectsByUserId(Long userId) {
        List<Project> projects = projectRepository.findByUserId(userId);
        return projects.stream()
                .map(projectMapper::convertToDTO) // Преобразование сущности в DTO
                .collect(Collectors.toList());
    }
    public ProjectDto deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Проект с ID " + id + " не найден"));
        projectRepository.deleteById(id);
        return projectMapper.convertToDTO(project);
    }


}