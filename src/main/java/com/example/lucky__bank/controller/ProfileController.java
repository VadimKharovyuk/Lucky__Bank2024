package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.ProfileRequest;
import com.example.lucky__bank.dto.ProfileDTO;
import com.example.lucky__bank.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileDTO> createOrUpdateProfile(@RequestBody ProfileRequest request) {
        ProfileDTO profileDTO = profileService.createOrUpdateProfile(request);
        return new ResponseEntity<>(profileDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        ProfileDTO profileDTO = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(profileDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}