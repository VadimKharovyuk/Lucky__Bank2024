package com.example.web.controller;

import com.example.web.dto.JobApplicationDto;
import com.example.web.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {


    private final JobApplicationService jobApplicationService ;


    //создать  заявку
    @PostMapping
    public String create(@ModelAttribute JobApplicationDto jobApplicationDto,
                         @RequestParam("resumeFile") MultipartFile resumeFile,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("form", jobApplicationDto);
            model.addAttribute("positions", jobApplicationService.getAllJobPositions());
            return "user/job/form";
        }

        try {
            if (!resumeFile.isEmpty()) {
                jobApplicationDto.setResume(resumeFile.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при загрузке файла резюме");
            model.addAttribute("form", jobApplicationDto);
            model.addAttribute("positions", jobApplicationService.getAllJobPositions());
            return "user/job/form";
        }

        try {
            jobApplicationService.create(jobApplicationDto);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при создании заявки: " + e.getMessage());
            model.addAttribute("form", jobApplicationDto);
            model.addAttribute("positions", jobApplicationService.getAllJobPositions());
            return "user/job/form";
        }

        return "redirect:/job/success";
    }
    @GetMapping
    public String home(){
        return "user/job/home";
    }

    @GetMapping("/form")
    public String form(Model model){
        var jobForm = new JobApplicationDto();
        model.addAttribute("form",jobForm);
        model.addAttribute("positions", jobApplicationService.getAllJobPositions());
        return "user/job/form";
    }


    @GetMapping("/success")
    public String success(){
        return "user/job/job_success";
    }

    //все заявки
    @GetMapping("/all")
    public String getAllJob(Model model){
        var listJob =jobApplicationService.listJob();
        model.addAttribute("list",listJob);
        return "user/job/list";
    }

    //по id
    @GetMapping("/{id}")
    public String getById(@PathVariable Long id ,Model model){
       var  byId = jobApplicationService.getById(id);
       model.addAttribute("id",byId);
        return "user/job/details";
    }

    // Получение резюме по ID
    @GetMapping("/{id}/resume")
    public ResponseEntity<byte[]> getResume(@PathVariable Long id) {
        byte[] resume = jobApplicationService.getResume(id);

        if (resume == null || resume.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf") // Задаем имя файла
                .contentType(MediaType.APPLICATION_PDF) //  заменить на MediaType.APPLICATION_PDF, если это PDF
                .body(resume);
    }

}
