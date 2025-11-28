package com.example.pdfGenerator.controller;

import com.example.pdfGenerator.model.LoginCredentials;
// import com.example.pdfGenerator.model.LoginCredentials;
import com.example.pdfGenerator.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

  @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginCredentials request) {
    String token = authService.login(request.getUsername(), request.getPassword());
    return ResponseEntity.ok(new LoginResponse(token));
}


    static class LoginRequest {
        public String username;
        public String password;
    }

    static class LoginResponse {
        public String token;
        public LoginResponse(String token) { this.token = token; }
    }
}
// Your HTML login form submits as application/x-www-form-urlencoded, not JSON.
