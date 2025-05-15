package com.ckiroshan.urlshortener.user.dto;

import com.ckiroshan.urlshortener.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterRequest {
    @NotBlank @Email // Validates email format
    private String email;
    @NotBlank @Size(min = 8) // Enforces a minimum password length
    private String password;
    @NotNull // Role must be provided; default is USER
    private UserRole role = UserRole.USER;
}