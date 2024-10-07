package com.example.lucky__bank.service;
import com.example.lucky__bank.Request.ProfileRequest;
import com.example.lucky__bank.dto.ProfileDTO;
import com.example.lucky__bank.maper.ProfileMapper;
import com.example.lucky__bank.model.Profile;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.ProfileRepository;
import com.example.lucky__bank.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final TransactionTemplate transactionTemplate;

    public ProfileDTO createOrUpdateProfile(ProfileRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(RuntimeException::new);

        Profile profile = profileRepository.findByUserId(request.getUserId())
                .orElse(new Profile());

        profile.setUser(user);
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());
        profile.setFullName(request.getFullName());
        profile.setDateOfBirth(String.valueOf(request.getDateOfBirth()));
        profile.setPassportNumber(request.getPassportNumber());
        profile.setEmploymentWorkPlace(request.getEmploymentWorkPlace());
        profile.setGender(Profile.Gender.valueOf(request.getGender()));
        profile.setMaritalStatus(Profile.MaritalStatus.valueOf(request.getMaritalStatus()));
        profile.setCitizenship(Profile.Citizenship.valueOf(request.getCitizenship()));

        Profile savedProfile = profileRepository.save(profile);
        return profileMapper.toDTO(savedProfile);
    }

    public ProfileDTO getProfileByUserId(Long userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException());
        return profileMapper.toDTO(profile);
    }
   // метод для обновления профиля
    public ProfileDTO updateProfile(Long userId, ProfileRequest request) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Обновление данных профиля
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setAddress(request.getAddress());
        profile.setFullName(request.getFullName());
        profile.setDateOfBirth(String.valueOf(request.getDateOfBirth()));
        profile.setPassportNumber(request.getPassportNumber());
        profile.setEmploymentWorkPlace(request.getEmploymentWorkPlace());
        profile.setGender(Profile.Gender.valueOf(request.getGender()));
        profile.setMaritalStatus(Profile.MaritalStatus.valueOf(request.getMaritalStatus()));
        profile.setCitizenship(Profile.Citizenship.valueOf(request.getCitizenship()));

        // Сохранение обновленного профиля
        Profile updatedProfile = profileRepository.save(profile);
        return profileMapper.toDTO(updatedProfile);
    }



    @Transactional
    public void saveUserPhoto(Long userId, MultipartFile file) throws IOException {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));
        profile.setProfilePicture(file.getBytes());
        profileRepository.save(profile);
        log.info("Photo saved for user: {}", userId);
    }

    public byte[] getUserPhoto(Long userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));
        byte[] photo = profile.getProfilePicture();
        if (photo == null || photo.length == 0) {
            log.warn("No photo found for user: {}", userId);
            return null;
        }
        log.info("Photo retrieved for user: {}", userId);
        return photo;
    }
}