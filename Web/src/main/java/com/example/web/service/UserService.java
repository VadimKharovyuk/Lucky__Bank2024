package com.example.web.service;

import com.example.web.dto.UserDTO;
import com.example.web.repository.UserFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserFeignClient userFeignClient;

    public List<UserDTO> userDTOList(){
        return userFeignClient.getAllUsers();
    }

    public UserDTO findById(Long userId) {
       return userFeignClient.getById(userId);
    }

    public UserDTO findByUsername(String name) {
      return   userFeignClient.getUserByUsername(name);
    }

    public UserDTO findByEmail(String email) {
       return userFeignClient.getUserByEmail(email);
    }
}
