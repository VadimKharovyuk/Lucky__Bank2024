package com.example.web.Request;

import lombok.Data;

@Data
public class ProjectCreateRequest {
    private String title;
    private String description;
    private String tokenType;
    private Long userId;
}
