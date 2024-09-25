package com.example.apipaymentservice.request;

import lombok.Data;

@Data
public class ProjectCreateRequestDto {


    private String title;
    private String description;
    private String tokenType;
    private Long userId;
}