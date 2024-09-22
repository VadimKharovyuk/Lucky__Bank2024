package com.example.lucky__bank.service;

import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    @Getter
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;



    public UserDTO login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(username);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return convertToDTO(user);
    }





    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword("OAuth2User_" + UUID.randomUUID().toString());
        }

        user.setRole(User.Role.USER);

        User newUser = userRepository.save(user);



        return newUser;
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
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

        // Проверка текущего пароля
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return false;
        }

        // Шифрование и установка нового пароля
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
            userRepository.save(user); // Убедитесь, что сохранение происходит внутри транзакции
        });
    }

    public boolean isBlocked(String username) {
        User user = findByUsername(username);
        return user != null && user.isBlocked();
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return (BCryptPasswordEncoder) passwordEncoder;
    }


    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToDTO);
    }

    public UserDTO findByUsernameDto(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? convertToDTO(user) : null;
    }


    //    private UserDTO convertToDTO(User user) {
//        UserDTO dto = new UserDTO();
//        dto.setId(user.getId());
//        dto.setUsername(user.getUsername());
//        dto.setEmail(user.getEmail());
//        dto.setRole(user.getRole().name()); // Преобразование перечисления в строку
//        dto.setBlocked(user.isBlocked());
//        // Не храните пароль в DTO по соображениям безопасности
//        return dto;
//    }
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .password(user.getPassword())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .blocked(user.isBlocked())
                .build();
    }
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(User.Role.valueOf(userDTO.getRole())); // Убедитесь, что роль правильно маппится
        user.setBlocked(userDTO.isBlocked());
        user.setPassword(userDTO.getPassword()); // Если пароль приходит из DTO, его нужно хэшировать
        return user;
    }

    public void save(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        userRepository.save(user);
    }


    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username); // Получаем пользователя или null
        if (user != null) {
            // Используем Builder для создания UserDTO
            return UserDTO.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .role(String.valueOf(user.getRole()))
                    .build();
        } else {
            return null;
        }
    }



}