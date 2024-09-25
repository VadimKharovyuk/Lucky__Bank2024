package com.example.web.repository;

import com.example.web.Request.ProjectCreateRequest;
import com.example.web.dto.ProjectDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "APIPAYMENTSERVICE", url = "http://localhost:1002")
public interface ApiPaymentServiceClient {


    @GetMapping("/api/projects/user/{id}")
    List<ProjectDto> getProjectbyUserId(@PathVariable Long id);

    @GetMapping("api/projects/all")
    List<ProjectDto> getAll();

    @PostMapping("/api/projects")
    ProjectDto createProject(@RequestBody ProjectCreateRequest projectCreateRequest);

    @GetMapping("/api/projects/{apiKey}")
    ProjectDto getProjectByApiKey(@PathVariable("apiKey") String apiKey);

    @PostMapping("/api/projects/{apiKey}/use-token")
    String useToken(@PathVariable("apiKey") String apiKey);


}
