package com.example.lucky__bank.controller;


import com.example.lucky__bank.dto.JobApplicationDto;
import com.example.lucky__bank.dto.JobPositionDto;
import com.example.lucky__bank.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    // Метод для создания новой заявки на работу
    @PostMapping
    public ResponseEntity<JobApplicationDto> createJobApplication(@RequestBody JobApplicationDto jobApplicationDto) {
        JobApplicationDto createdJobApplication = jobApplicationService.createJobApplication(jobApplicationDto);
        return new ResponseEntity<>(createdJobApplication, HttpStatus.CREATED);
    }


    //поиск всех позиций
    @GetMapping
    public ResponseEntity<List<JobPositionDto>>  getAll(){
        List<JobPositionDto> getAll = jobApplicationService.getAll();
        return ResponseEntity.ok(getAll);
    }

    //поиск всех заявок
    @GetMapping("/all")
    public ResponseEntity<List<JobApplicationDto>> listJob(){
       List<JobApplicationDto> applicationDto = jobApplicationService.applicationDtoList();
       return ResponseEntity.ok(applicationDto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationDto> getById(@PathVariable Long id){
        JobApplicationDto getBYid =  jobApplicationService.getById(id);
        return ResponseEntity.ok(getBYid);
    }
}