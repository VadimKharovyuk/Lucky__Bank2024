package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .blocked(user.isBlocked())
                .build();
    }

    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(User.Role.valueOf(userDTO.getRole()));
        user.setBlocked(userDTO.isBlocked());
        // Убедитесь, что пароль хэшируется при регистрации
        return user;
    }
}