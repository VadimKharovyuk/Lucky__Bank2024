package com.example.web.service;

import com.example.web.dto.JobApplicationDto;
import com.example.web.dto.JobPositionDto;
import com.example.web.repository.JobApplicationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationService {
    private final JobApplicationServiceClient jobApplicationServiceClient;

    public JobApplicationDto create(JobApplicationDto jobApplicationDto){
        jobApplicationDto.setApplicationDate(LocalDate.now());
       return jobApplicationServiceClient.create(jobApplicationDto);
    }

    public List<JobPositionDto> getAllJobPositions() {
        return jobApplicationServiceClient.getAllPosition();

    }
}
