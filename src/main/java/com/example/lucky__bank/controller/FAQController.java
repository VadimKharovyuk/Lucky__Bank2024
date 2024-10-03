package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.FAQRequest;
import com.example.lucky__bank.dto.FAQDto;
import com.example.lucky__bank.service.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
public class FAQController {
    private final FAQService faqService;

    @PostMapping
    public FAQDto createFAQ(@RequestBody FAQRequest request) {
        return faqService.create(request);
    }

    @GetMapping
    public List<FAQDto> getAllActiveFAQs() {
        return faqService.getAllActiveFAQs();
    }
}