package com.ckiroshan.urlshortener.user.mapper;

import com.ckiroshan.urlshortener.user.dto.RegisterRequest;
import com.ckiroshan.urlshortener.user.entity.User;
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
}