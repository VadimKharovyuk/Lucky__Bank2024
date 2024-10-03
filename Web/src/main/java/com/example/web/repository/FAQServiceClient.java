package com.example.web.repository;

import com.example.web.dto.FAQDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "FAQt-Service", url = "http://localhost:1000")
public interface FAQServiceClient {
    @GetMapping("/api/faq")
    List<FAQDto> list();
}
