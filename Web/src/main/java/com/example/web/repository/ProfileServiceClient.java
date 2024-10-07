package com.example.web.repository;

import com.example.web.Request.ProfileRequest;
import com.example.web.dto.ProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@FeignClient(name = "ProfileService", url = "http://localhost:1000")
public interface ProfileServiceClient {



    @GetMapping("/api/profiles/{id}/birthday")
    Optional<String> getBirthday(@PathVariable Long id);

    @PostMapping(value = "/api/profiles/{userId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadPhoto(@PathVariable Long userId, @RequestPart("file") MultipartFile file);

    @GetMapping("/api/profiles/{userId}/photo")
    byte[] getPhoto(@PathVariable Long userId);



    @PostMapping("/api/profiles/update/{userId}")
    ProfileDTO update(@PathVariable Long userId, @RequestBody ProfileRequest profileRequest);


    @GetMapping("/api/profiles/{userId}")
    ProfileDTO getProfileByUserId(@PathVariable Long userId);

    @PostMapping("/api/profiles")
    ProfileDTO createProfile(@RequestBody ProfileRequest profileRequest);



}
