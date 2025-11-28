package com.example.pdfGenerator.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "phil2005"; // the password you want
        String hashed = encoder.encode(rawPassword);
        System.out.println("BCrypt hashed password: " + hashed);
    }
}
