package com.example.lucky__bank.maper;


import com.example.lucky__bank.dto.FactDto;
import com.example.lucky__bank.model.Fact;
import org.springframework.stereotype.Component;

@Component
 public class FactMapper {

    public FactDto toDto(Fact fact){
        return FactDto.builder()
                .id(fact.getId())
                .type(fact.getType())
                .content(fact.getContent()).build();
    }

        public Fact toEntity(FactDto dto) {
            if (dto == null) {
                return null;
            }

            Fact fact = new Fact();
            fact.setId(dto.getId());
            fact.setContent(dto.getContent());
            fact.setType(dto.getType());
            return fact;
        }
    }

