package com.example.lucky__bank.service;

import com.example.lucky__bank.dto.JobApplicationDto;
import com.example.lucky__bank.dto.JobPositionDto;
import com.example.lucky__bank.maper.JobApplicationMapper;
import com.example.lucky__bank.maper.JobPositionMapper;
import com.example.lucky__bank.model.JobApplication;
import com.example.lucky__bank.model.JobPosition;
import com.example.lucky__bank.repository.JobApplicationRepository;
import com.example.lucky__bank.repository.JobPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobApplicationService {
    private final JobApplicationMapper jobApplicationMapper;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobPositionRepository jobPositionRepository;
    private final JobPositionMapper jobPositionMapper;

    public JobApplicationDto createJobApplication(JobApplicationDto jobApplicationDto) {
        // Проверяем, что appliedPosition не null и не пустая строка
        if (jobApplicationDto.getAppliedPosition() == null || jobApplicationDto.getAppliedPosition().isEmpty()) {
            throw new IllegalArgumentException("Applied position cannot be empty");
        }

        // Находим позицию по ID
        JobPosition jobPosition = jobPositionRepository.findById(Long.valueOf(jobApplicationDto.getAppliedPosition()))
                .orElseThrow(() -> new RuntimeException("Должность не найдена"));

        // Преобразуем DTO в сущность
        JobApplication jobApplication = jobApplicationMapper.toEntity(jobApplicationDto, jobPosition);

        // Установка текущей даты, если она не передана
        if (jobApplication.getApplicationDate() == null) {
            jobApplication.setApplicationDate(LocalDate.now());
        }

        // Сохраняем сущность в базе данных
        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        // Преобразуем обратно в DTO и возвращаем
        return jobApplicationMapper.toDto(savedJobApplication);
    }

    public List<JobApplicationDto> applicationDtoList() {
        List<JobApplication> list = jobApplicationRepository.findAll();
        return list.stream()
                .map(jobApplicationMapper::toDto)
                .collect(Collectors.toList());
    }
//все позиции
    public List<JobPositionDto> getAll() {
        List<JobPosition> jobPosition = jobPositionRepository.findAll();
        return jobPosition.stream()
                .map(jobPositionMapper::toDto)
                .collect(Collectors.toList());


    }
//заявка по id

    public JobApplicationDto getById(Long id) {
        JobApplication getById = jobApplicationRepository.findById(id).orElseThrow(() -> new RuntimeException());
        return jobApplicationMapper.toDto(getById);
    }
//резюме по id
    public byte[] getResume(Long applicationId) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));
        return application.getResume();
    }
}
