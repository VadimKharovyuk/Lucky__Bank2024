package com.example.web.dto;

import lombok.Data;

@Data
public class ProfileDTO {

    private String phoneNumber;
    private String address;
    private String fullName;
    private String dateOfBirth;
    private String passportNumber;
    private String employmentWorkPlace;

    private String gender;
    private MaritalStatus maritalStatus;
    private String  citizenship;

    private byte[] profilePicture;


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
