package com.example.web.Request;

import com.example.web.dto.ProfileDTO;
import lombok.Data;

import java.time.LocalDate;
@Data

public class ProfileRequest {
    private Long userId;

    private String phoneNumber;
    private String address;
    private String fullName;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private String employmentWorkPlace;
    private String gender;
    private String maritalStatus;
    private String citizenship;


}

