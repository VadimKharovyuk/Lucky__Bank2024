package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.ProfileDTO;
import com.example.lucky__bank.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {
    public ProfileDTO toDTO (Profile profile){
        return ProfileDTO.builder()

                .maritalStatus(profile.getMaritalStatus().toString())
                .gender(profile.getGender().toString())
                .address(profile.getAddress())
                .dateOfBirth(profile.getDateOfBirth())
                .fullName(profile.getFullName())
                .passportNumber(profile.getPassportNumber())
                .phoneNumber(profile.getPhoneNumber())
                .employmentWorkPlace(profile.getEmploymentWorkPlace())
                .citizenship(profile.getCitizenship().toString())
                .profilePicture(profile.getProfilePicture())

                .build();
    }
}
