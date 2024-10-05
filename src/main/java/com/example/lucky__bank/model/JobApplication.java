package com.example.lucky__bank.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] resume;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;



    private String university;
    private String degree;
    private int graduationYear;

    private String currentPosition;
    private String currentCompany;

    @Column(length = 1000)
    private String workExperience;

    @ElementCollection
    private List<String> skills;

    @ManyToOne
    private JobPosition appliedPosition;

    private LocalDate availableStartDate;

    private boolean willingToRelocate;

    @Column(length = 1000)
    private String additionalInfo;



    private LocalDate applicationDate;



    private String linkedinProfile;

    private boolean agreedToTerms;


    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;

    @Enumerated(EnumType.STRING)
    private PreferredContactMethod preferredContactMethod;

    public enum EducationLevel {
        HIGH_SCHOOL, BACHELORS, MASTERS, PHD, OTHER
    }

    public enum PreferredContactMethod {
        EMAIL, PHONE, BOTH
    }

}

