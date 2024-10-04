package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.JobPositionDto;
import com.example.lucky__bank.model.JobPosition;
import org.springframework.stereotype.Component;

@Component
public class JobPositionMapper {
    public JobPositionDto toDto(JobPosition jobPosition){
        return JobPositionDto.builder()
                .id(jobPosition.getId())
                .position(jobPosition.getPosition())
                .build();
    }
}
