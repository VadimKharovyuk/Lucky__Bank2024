package com.example.lucky__bank.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class JobApplicationDto {
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;

    private String educationLevel;

    private String university;
    private String degree;
    private int graduationYear;

    private String currentPosition;
    private String currentCompany;

    private String workExperience;

    private List<String> skills;

    private String appliedPosition; // Можно использовать String, если должность будет идентифицироваться по названию

    private LocalDate availableStartDate;

    private boolean willingToRelocate;

    private String additionalInfo;

    private LocalDate applicationDate;

    private String linkedinProfile;

    private boolean agreedToTerms;


    private String preferredContactMethod;
}