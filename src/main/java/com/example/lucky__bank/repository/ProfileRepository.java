package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Optional<Profile> findByUserId(Long userId);
}
