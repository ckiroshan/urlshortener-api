package com.ckiroshan.urlshortener.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter @Builder
public class AdminUrlResponse {
    // Response DTO for URL details in the admin dashboard
    private String shortCode;
    private String originalUrl;
    private String createdBy;
    private LocalDateTime createdAt;
    private int clickCount;
}