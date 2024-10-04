package com.example.web.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class JobApplicationDto {
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;

    private EducationLevel educationLevel;

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


    private String resumeFileName; ///lob


    private PreferredContactMethod preferredContactMethod;

    public enum EducationLevel {
        HIGH_SCHOOL, BACHELORS, MASTERS, PHD, OTHER
    }

    public enum PreferredContactMethod {
        EMAIL, PHONE, BOTH
    }
}
