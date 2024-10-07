package com.example.lucky__bank.dto;

import com.example.lucky__bank.model.Profile;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProfileDTO {

    private String phoneNumber;
    private String address;
    private String fullName;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private String employmentWorkPlace;
    private String gender;
    private String maritalStatus;
    private String  citizenship;


    private byte[] profilePicture;
}
