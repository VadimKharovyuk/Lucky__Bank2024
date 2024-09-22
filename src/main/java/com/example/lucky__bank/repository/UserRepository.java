package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
