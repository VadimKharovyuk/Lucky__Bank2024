package com.example.web.controller;

import com.example.web.dto.JobApplicationDto;
import com.example.web.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/job")
public class JobController {

    private final JobApplicationService jobApplicationService ;
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
//создать  заявку
    @PostMapping
    public String create( @ModelAttribute JobApplicationDto jobApplicationDto, BindingResult result, Model model){
        if (result.hasErrors()) {
            model.addAttribute("form", jobApplicationDto);
            model.addAttribute("positions", jobApplicationService.getAllJobPositions());
            return "user/job/form";
        }

        jobApplicationService.create(jobApplicationDto);
        return "redirect:/job/success";
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
}
