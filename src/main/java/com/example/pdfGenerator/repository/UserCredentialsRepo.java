package com.example.pdfGenerator.repository;

import com.example.pdfGenerator.model.LoginCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserCredentialsRepo extends JpaRepository<LoginCredentials, Long> {
    Optional<LoginCredentials> findByUsername(String username);
}
