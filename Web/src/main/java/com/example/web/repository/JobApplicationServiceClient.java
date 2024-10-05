package com.example.web.repository;

import com.example.web.dto.JobApplicationDto;
import com.example.web.dto.JobPositionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "JobApplication-Service", url = "http://localhost:1000")
public interface JobApplicationServiceClient {

    @PostMapping("/api/job-applications")
    JobApplicationDto create(@RequestBody JobApplicationDto jobApplicationDto);


    @GetMapping("/api/job-applications")
    List<JobPositionDto> getAllPosition();

    @GetMapping("/api/job-applications/all")
    List<JobApplicationDto> listJob();

    @GetMapping("/api/job-applications/{id}")
    JobApplicationDto getById(@PathVariable Long id);

    @GetMapping("/api/job-applications/{id}/resume")
    byte[] getResume(@PathVariable Long id);


}