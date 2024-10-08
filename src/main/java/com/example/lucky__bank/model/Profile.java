package com.example.lucky__bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Locale;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Lob
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String address;
    private String fullName;
    private String passportNumber;
    private String employmentWorkPlace;





    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    private Citizenship citizenship;


    public enum MaritalStatus {
        SINGLE, MARRIED, DIVORCED
    }
    public enum Gender {
        MALE, FEMALE
    }
    public enum Citizenship {
        ARGENTINA,
        AUSTRALIA,
        BRAZIL,
        CANADA,
        CHINA,
        FRANCE,
        GERMANY,
        ITALY,
        JAPAN,
        MEXICO,
        NETHERLANDS,
        NORWAY,
        RUSSIA,
        SOUTH_AFRICA,
        SPAIN,
        SWEDEN,
        SWITZERLAND,
        UKRAINE,
        UNITED_KINGDOM,
        UNITED_STATES;
    }
}
