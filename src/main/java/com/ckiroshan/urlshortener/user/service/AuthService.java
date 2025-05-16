package com.ckiroshan.urlshortener.user.service;

import com.ckiroshan.urlshortener.user.dto.AuthResponse;
import com.ckiroshan.urlshortener.user.dto.RegisterRequest;

public interface AuthService {
    // Handles user registration
    AuthResponse register(RegisterRequest request);
}