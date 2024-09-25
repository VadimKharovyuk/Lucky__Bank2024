package com.example.web.service;

import com.example.web.Request.ProjectCreateRequest;
import com.example.web.dto.ProjectDto;
import com.example.web.repository.ApiPaymentServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiPaymentService {
    private final ApiPaymentServiceClient apiPaymentServiceClient;

    public ProjectDto createProject(ProjectCreateRequest projectCreateRequest){
    return   apiPaymentServiceClient.createProject(projectCreateRequest);

    }

    public ProjectDto getProjectByApiKey(String apiKey) {
        return apiPaymentServiceClient.getProjectByApiKey(apiKey);
    }

    public String useToken(String apiKey) {
        return apiPaymentServiceClient.useToken(apiKey);
    }
}
