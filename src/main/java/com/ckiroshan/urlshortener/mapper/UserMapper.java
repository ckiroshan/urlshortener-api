package com.ckiroshan.urlshortener.mapper;

import com.ckiroshan.urlshortener.dto.user.AuthResponse;
import com.ckiroshan.urlshortener.dto.user.RegisterRequest;
import com.ckiroshan.urlshortener.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // Converts RegisterRequest DTO to User entity
    public User registerDTOtoEntity(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword()) // before encoding password
                .role(request.getRole())
                .build();
    }

    // Converts User entity and JWT token to AuthResponse DTO
    public AuthResponse authDTOtoAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}