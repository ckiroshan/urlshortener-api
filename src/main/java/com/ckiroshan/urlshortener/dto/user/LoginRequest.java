package com.ckiroshan.urlshortener.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginRequest {
    @NotBlank // Validation: ensures the field is not null or empty
    @Email // Validation: ensures the string is in valid email format
    private String email;
    @NotBlank @Size(min = 8) // Validation: password must be at least 8 characters
    private String password;
}