package com.example.apipaymentservice.repository;

import com.example.apipaymentservice.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    Optional<Project> findByApiKey(String apiKey);
}
