package com.example.lucky__bank.dto;

import com.example.lucky__bank.model.Profile;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDTO {

    private String phoneNumber;
    private String address;
    private String fullName;
    private  String dateOfBirth;
    private String passportNumber;
    private String employmentWorkPlace;
    private String gender;
    private String maritalStatus;
    private String  citizenship;

}
