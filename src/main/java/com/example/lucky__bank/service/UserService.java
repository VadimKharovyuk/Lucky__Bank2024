package com.example.lucky__bank.service;

import com.example.lucky__bank.Request.ProfileRequest;
import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.Request.UserRegistrationRequest;
import com.example.lucky__bank.maper.UserMapper;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    @Getter
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;


    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.convertToDTO(user);
    }

    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::convertToDTO);
    }

    public UserDTO login(String username, String password) {
        User user = userRepository.findByUsername(username);
        log.info("Stored password: {}", user.getPassword());
        log.info("Password matches: {}", passwordEncoder.matches(password, user.getPassword()));
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return userMapper.convertToDTO(user);
    }


    public UserDTO registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        User newUser = userRepository.save(user);

        return userMapper.convertToDTO(newUser);
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? userMapper.convertToDTO(user) : null;
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return false;
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true;
    }

    @Transactional
    public void blockUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setBlocked(true);
            userRepository.save(user);
        });
    }

    @Transactional
    public void unblockUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            user.setBlocked(false);
            userRepository.save(user);
        });
    }

    public boolean isBlocked(String username) {
        User user = userRepository.findByUsername(username);
        return user != null && user.isBlocked();
    }

    public UserDTO findByUsernameDto(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? userMapper.convertToDTO(user) : null;
    }



    public User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}