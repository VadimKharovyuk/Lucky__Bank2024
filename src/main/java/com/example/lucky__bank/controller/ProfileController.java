package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.ProfileRequest;
import com.example.lucky__bank.dto.ProfileDTO;
import com.example.lucky__bank.service.BirthdayService;
import com.example.lucky__bank.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/profiles")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final BirthdayService birthdayService;

    @GetMapping("/{id}/birthday")
    public ResponseEntity<String> birthday(@PathVariable Long id) {
        try {
            ProfileDTO profileDTO = profileService.getProfileByUserId(id);
            return birthdayService.checkBirthday(profileDTO.getDateOfBirth())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.noContent().build());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            profileService.saveUserPhoto(userId, file);
            return ResponseEntity.ok("Фотография успешно загружена");
        } catch (IOException e) {
            log.error("Error uploading photo for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при загрузке фотографии");
        }
    }

    @GetMapping("/{userId}/photo")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long userId) {
        try {
            byte[] photo = profileService.getUserPhoto(userId);
            if (photo != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(photo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            log.error("Error retrieving photo for user {}: {}", userId, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
//create
    @PostMapping
    public ResponseEntity<ProfileDTO> createOrUpdateProfile(@RequestBody ProfileRequest request) {
        ProfileDTO profileDTO = profileService.createOrUpdateProfile(request);
        return new ResponseEntity<>(profileDTO, HttpStatus.CREATED);
    }
//поиск профиля
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