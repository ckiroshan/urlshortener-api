package com.ckiroshan.urlshortener.dto.admin;

import com.ckiroshan.urlshortener.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class UserResponse {
    // Response DTO representing basic user information
    private Long id;
    private String email;
    private UserRole role;
    private boolean active;
}