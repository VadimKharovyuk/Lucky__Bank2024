package com.example.lucky__bank.controller;

import com.example.lucky__bank.Request.LoginRequest;
import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.Request.UserRegistrationRequest;
import com.example.lucky__bank.service.EmailService;
import com.example.lucky__bank.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final EmailService emailService;


    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        log.info("Fetching user with id: {}", id);
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok(userDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDTO user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            log.info("User logged in: {}", user.getUsername());

            return ResponseEntity.ok(user);
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            // Проверка существования email
            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            UserDTO newUserDTO = userService.registerUser(request);
            emailService.sendRegistrationEmail(newUserDTO.getEmail());
            return ResponseEntity.ok(newUserDTO);
        } catch (Exception e) {
            log.error("Ошибка регистрации: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }




    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        log.info("Received request to get user by username: {}", username);
        UserDTO userDTO = userService.findByUsernameDto(username);
        if (userDTO == null) {
            log.warn("User not found for username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(userDTO);
    }


    @GetMapping("/by-email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        return userService.findUserByEmail(email)
                .map(userDTO -> ResponseEntity.ok(userDTO))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String username,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        boolean success = userService.changePassword(username, currentPassword, newPassword);
        if (success) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Current password is incorrect");
        }
    }

    @PostMapping("/block/{userId}")
    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
        userService.blockUser(userId);
        return ResponseEntity.ok("User blocked successfully");
    }


    @PostMapping("/unblock/{userId}")
    public ResponseEntity<String> unblockUser(@PathVariable Long userId) {
        userService.unblockUser(userId);
        return ResponseEntity.ok("User unblocked successfully");
    }

    @GetMapping("/is-blocked")
    public ResponseEntity<Boolean> isBlocked(@RequestParam String username) {
        boolean blocked = userService.isBlocked(username);
        return ResponseEntity.ok(blocked);
    }


}