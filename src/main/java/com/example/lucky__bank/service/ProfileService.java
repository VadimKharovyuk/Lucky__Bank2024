package com.example.lucky__bank.service;
import com.example.lucky__bank.Request.ProfileRequest;
import com.example.lucky__bank.dto.ProfileDTO;
import com.example.lucky__bank.maper.ProfileMapper;
import com.example.lucky__bank.model.Profile;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.ProfileRepository;
import com.example.lucky__bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

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
}