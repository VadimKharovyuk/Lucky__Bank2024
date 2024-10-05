package com.example.web.service;

import com.example.web.dto.JobApplicationDto;
import com.example.web.dto.JobPositionDto;
import com.example.web.repository.JobApplicationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {
    private final JobApplicationServiceClient jobApplicationServiceClient;

    public JobApplicationDto create(JobApplicationDto jobApplicationDto) {
        jobApplicationDto.setApplicationDate(LocalDate.now());
        return jobApplicationServiceClient.create(jobApplicationDto);
    }

    public List<JobPositionDto> getAllJobPositions() {
        return jobApplicationServiceClient.getAllPosition();

    }

    public List<JobApplicationDto> listJob() {
        return jobApplicationServiceClient.listJob();
    }

    public JobApplicationDto getById(Long id) {
        return jobApplicationServiceClient.getById(id);

    }

    public byte[] getResume(Long id) {
        return jobApplicationServiceClient.getResume(id);
    }
}
