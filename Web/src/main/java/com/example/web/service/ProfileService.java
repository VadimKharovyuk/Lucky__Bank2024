package com.example.web.service;

import com.example.web.Request.ProfileRequest;
import com.example.web.dto.ProfileDTO;
import com.example.web.repository.ProfileServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileServiceClient profileServiceClient;


    @Transactional
    public String uploadPhoto(Long userId, MultipartFile file) {
        try {
            log.info("Uploading photo for user: {}", userId);
            return profileServiceClient.uploadPhoto(userId, file);
        } catch (FeignException e) {
            log.error("Error uploading photo for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Ошибка при загрузке фотографии", e);
        }
    }

    @Transactional
    public byte[] getProfilePhoto(Long userId) {
        try {
            log.info("Retrieving photo for user: {}", userId);
            return profileServiceClient.getPhoto(userId);
        } catch (FeignException e) {
            log.error("Error retrieving photo for user {}: {}", userId, e.getMessage());
            return null;
        }
    }


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


    public Optional<String> checkBirthday(Long id) {
        return profileServiceClient.getBirthday(id);


    }

}
