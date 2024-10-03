package com.example.lucky__bank.service;

import com.example.lucky__bank.Request.FAQRequest;
import com.example.lucky__bank.dto.FAQDto;
import com.example.lucky__bank.maper.FAQMapper;
import com.example.lucky__bank.model.FAQ;
import com.example.lucky__bank.repository.FAQRepository;
import com.example.lucky__bank.repository.FactRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FAQService {
    private final FAQRepository faqRepository;
    private final FAQMapper mapper;


    public FAQDto create(FAQRequest faqRequest) {
        FAQ faq = new FAQ();
        faq.setTopic(faqRequest.getTopic());
        faq.setQuestion(faqRequest.getQuestion());
        faq.setAnswer(faqRequest.getAnswer());
        faq.setActive(true);

        FAQ savedFaq = faqRepository.save(faq);
        return mapper.toDto(savedFaq);
    }
    public List<FAQDto> getAllActiveFAQs() {
        List<FAQDto> faqs = faqRepository.findAll().stream()
                .filter(FAQ::isActive)
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return faqs;
    }

    public FAQDto updateFAQ(Long id, FAQRequest faqRequest) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));

        faq.setTopic(faqRequest.getTopic());
        faq.setQuestion(faqRequest.getQuestion());
        faq.setAnswer(faqRequest.getAnswer());

        FAQ updatedFaq = faqRepository.save(faq);
        return mapper.toDto(updatedFaq);
    }

    public void deactivateFAQ(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));

        faq.setActive(false);
        faqRepository.save(faq);
    }
}
