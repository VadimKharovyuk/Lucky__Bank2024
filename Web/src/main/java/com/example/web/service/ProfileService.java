package com.example.web.service;

import com.example.web.Request.ProfileRequest;
import com.example.web.dto.ProfileDTO;
import com.example.web.repository.ProfileServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileServiceClient profileServiceClient;


    public ProfileDTO getProfileByUserId(Long userId) {
        return profileServiceClient.getProfileByUserId(userId);
    }
    public ProfileDTO crateProfile(ProfileRequest profileRequest) {
        return profileServiceClient.createProfile(profileRequest);
    }
    public ProfileDTO update(Long userId, ProfileRequest profileRequest) {
        ProfileDTO profileDTO = profileServiceClient.getProfileByUserId(userId);
        return profileServiceClient.update(userId, profileRequest);
    }




}
