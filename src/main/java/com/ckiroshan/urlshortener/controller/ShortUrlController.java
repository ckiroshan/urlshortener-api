package com.ckiroshan.urlshortener.controller;

import com.ckiroshan.urlshortener.dto.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.ShortUrlResponse;
import com.ckiroshan.urlshortener.exception.BadRequestException;
import com.ckiroshan.urlshortener.service.ShortUrlService;
import com.ckiroshan.urlshortener.user.entity.User;
import com.ckiroshan.urlshortener.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shorten") // Base path for all URL shortening endpoints
@RequiredArgsConstructor // Generates constructor for final fields
public class ShortUrlController {
    private final ShortUrlService shortUrlService;
    private final UserRepository userRepository;

    @PostMapping // Endpoint to create a shortened URL
    public ResponseEntity<ShortUrlResponse> createShortUrl(
            @Valid @RequestBody ShortUrlRequest shortUrlRequest, // Validates and binds request body to DTO
            @AuthenticationPrincipal UserDetails userDetails) { // Injects authenticated user
        // Convert Spring Security's UserDetails to your User entity
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));  // Handles possible missing user
        // Returns a response containing the shortened URL & related info
        return ResponseEntity.ok(shortUrlService.createShortUrl(shortUrlRequest, user));
    }
}
