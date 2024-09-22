package com.example.web.repository;

import com.example.web.dto.LoginRequest;
import com.example.web.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "lucky-bank", url = "http://192.168.1.105:1000")
public interface UserFeignClient {

    @PostMapping("/api/users/login")
    ResponseEntity<UserDTO> login(@RequestBody LoginRequest loginRequest);

    @GetMapping("/api/users/{username}")
    UserDTO getUserByUsername(@PathVariable String username);

    @PostMapping("/api/users/register")
    UserDTO registerUser(@RequestBody UserDTO userDTO);

    @GetMapping("/api/users/by-email")
    UserDTO getUserByEmail(@RequestParam String email);



    @GetMapping("/api/users")
    List<UserDTO> getAllUsers();


    @PutMapping("/api/users/change-password")
    String changePassword(
            @RequestParam String username,
            @RequestParam String currentPassword,
            @RequestParam String newPassword);

    @PostMapping("/api/users/block/{userId}")
    String blockUser(@PathVariable Long userId);

    @PostMapping("/api/users/unblock/{userId}")
    String unblockUser(@PathVariable Long userId);

    @GetMapping("/api/users/is-blocked")
    Boolean isBlocked(@RequestParam String username);

    @PostMapping("/api/password/reset")
    ResponseEntity<String> sendEmailPassword(
            @RequestParam String email,
            @RequestParam String newPassword);

    @DeleteMapping("/api/users/delete/{id}")
    ResponseEntity<Void> deleteUserById(@PathVariable long id);

}
