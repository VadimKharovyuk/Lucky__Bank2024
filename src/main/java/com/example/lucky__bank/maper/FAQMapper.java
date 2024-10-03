package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.FAQDto;
import com.example.lucky__bank.model.FAQ;
import com.example.lucky__bank.model.Fact;
import org.springframework.stereotype.Component;

@Component
public class FAQMapper {
    public FAQDto toDto(FAQ faq){
        return FAQDto.builder()
                .id(faq.getId())

                .answer(faq.getAnswer())
                .topic(faq.getTopic())
                .isActive(faq.isActive())
                .question(faq.getQuestion())
                .build();


    }
}
