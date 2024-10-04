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
    private final JobApplicationMapper  jobApplicationMapper;
    private final JobApplicationRepository jobApplicationRepository;
    private final JobPositionRepository jobPositionRepository;
    private final JobPositionMapper jobPositionMapper;



    public List<JobPositionDto> getAll(){
        List<JobPosition> jobPosition = jobPositionRepository.findAll();
        return  jobPosition.stream()
                .map(jobPositionMapper::toDto)
                .collect(Collectors.toList());


    }
//    public void create(JobApplicationDto jobApplicationDto, MultipartFile resume) throws IOException {
//        JobApplication jobApplication = jobApplicationMapper.toEntity(jobApplicationDto);
//
//        if (resume != null && !resume.isEmpty()) {
//            // Проверка размера файла
//            if (resume.getSize() > 5 * 1024 * 1024) { // например, ограничение в 5 МБ
//                throw new IllegalArgumentException("Размер файла превышает допустимый лимит");
//            }
//
//            // Проверка типа файла
//            String contentType = resume.getContentType();
//            if (contentType == null || !(contentType.equals("application/pdf") ||
//                    contentType.equals("application/msword") ||
//                    contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
//                throw new IllegalArgumentException("Недопустимый формат файла");
//            }
//
//            byte[] resumeBytes = resume.getBytes();
//            jobApplication.setResume(resumeBytes);
//        }
//
//        jobApplication.setApplicationDate(LocalDate.now());
//        jobApplicationRepository.save(jobApplication);
//    }

    public JobApplicationDto createJobApplication(JobApplicationDto jobApplicationDto) {
        // Находим позицию по ID
        JobPosition jobPosition = jobPositionRepository.findById(Long.valueOf(jobApplicationDto.getAppliedPosition()))
                .orElseThrow(() -> new RuntimeException("Должность не найдена")); // Обработка ошибки

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
}
