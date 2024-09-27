package com.example.web.repository;

import com.example.web.Request.ProfileRequest;
import com.example.web.dto.ProfileDTO;
import com.example.web.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ProfileService", url = "http://localhost:1000")
public interface ProfileServiceClient {


    @GetMapping("/api/profiles/{userId}")
    ProfileDTO getProfileByUserId(@PathVariable Long userId);

    @PostMapping("/api/profiles")
    ProfileDTO createProfile(@RequestBody ProfileRequest profileRequest );




}
