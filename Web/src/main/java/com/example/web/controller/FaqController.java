package com.example.web.controller;

import com.example.web.dto.FAQDto;
import com.example.web.service.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FaqController {
    private final FAQService service;

    @GetMapping
    public String faqList(Model model){
        FAQDto faqDto = new FAQDto();
        var faq = service.list();
        model.addAttribute("list",faq);
        System.out.println("name " + faqDto.getAnswer());

       return  "user/FAQ/FAQ";
    }
}
