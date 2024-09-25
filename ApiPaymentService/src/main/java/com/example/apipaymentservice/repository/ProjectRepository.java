package com.example.apipaymentservice.repository;

import com.example.apipaymentservice.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    List<Project> findByUserId(Long userId);
    Optional<Project> findByApiKey(String apiKey);
    Optional<Project> getProjectsById(Long id);


}
