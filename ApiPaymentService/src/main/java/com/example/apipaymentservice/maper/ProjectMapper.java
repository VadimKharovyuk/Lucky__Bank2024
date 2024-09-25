package com.example.apipaymentservice.maper;

import com.example.apipaymentservice.dto.ProjectDto;
import com.example.apipaymentservice.model.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public ProjectDto convertToDTO(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .apiKey(project.getApiKey())
                .title(project.getTitle())
                .description(project.getDescription())
                .lastResetDate(project.getLastResetDate()) // время последнего использвания
                .createdAt(project.getCreatedAt()) // передаем время создания
                .updatedAt(project.getUpdatedAt()) // передаем время последнего обновления
                .tokens(project.getTokens())
                .tokenType(project.getTokenType())
                .userId(project.getUserId())
                .build();
    }
}
