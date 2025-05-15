package com.ckiroshan.urlshortener.user.dto;

import com.ckiroshan.urlshortener.user.entity.UserRole;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    private String token; // JWT token
    private String email; // User's email ID
    private UserRole role; // User's role (USER/ADMIN) for role-based access
}