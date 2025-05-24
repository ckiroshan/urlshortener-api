package com.ckiroshan.urlshortener.service;

import com.ckiroshan.urlshortener.dto.user.AuthResponse;
import com.ckiroshan.urlshortener.dto.user.LoginRequest;
import com.ckiroshan.urlshortener.dto.user.RegisterRequest;

public interface AuthService {
    // Handles user registration
    AuthResponse register(RegisterRequest request);
    // Handles user login
    AuthResponse login(LoginRequest request);
}