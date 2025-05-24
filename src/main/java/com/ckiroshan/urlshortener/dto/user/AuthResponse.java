package com.ckiroshan.urlshortener.dto.user;

import com.ckiroshan.urlshortener.entity.UserRole;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponse {
    private String token; // JWT token
    private String email; // User's email ID
    private UserRole role; // User's role (USER/ADMIN) for role-based access
}