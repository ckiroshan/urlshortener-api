package com.ckiroshan.urlshortener.user.controller;

import com.ckiroshan.urlshortener.user.dto.AuthResponse;
import com.ckiroshan.urlshortener.user.dto.RegisterRequest;
import com.ckiroshan.urlshortener.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Automatically injects final dependencies via constructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register") // Endpoint for user registration
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        // Call service to register new user & return the JWT response
        return ResponseEntity.ok(authService.register(request));
    }
}