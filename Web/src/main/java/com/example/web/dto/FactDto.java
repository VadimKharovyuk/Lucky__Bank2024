package com.example.web.dto;

import lombok.Data;

@Data
public class FactDto {

    private Long id;
    private String content;

    private FactType type;

    public enum FactType {
        JAVA, // факты о Java
        UKRAINE, // факты о Украине
        MISC;



    }

}
