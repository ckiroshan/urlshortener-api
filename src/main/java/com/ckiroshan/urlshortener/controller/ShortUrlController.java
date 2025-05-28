package com.ckiroshan.urlshortener.controller;

import com.ckiroshan.urlshortener.dto.shorturl.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.shorturl.ShortUrlResponse;
import com.ckiroshan.urlshortener.service.ShortUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") // Base path for all URL shortening endpoints
@RequiredArgsConstructor // Generates constructor for final fields
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping("/shorten") // Endpoint to create a shortened URL
    public ResponseEntity<ShortUrlResponse> createShortUrl(
            @Valid @RequestBody ShortUrlRequest shortUrlRequest, // Validates and binds request body to DTO
            @AuthenticationPrincipal UserDetails userDetails) { // Injects authenticated user
        // Returns a response containing the shortened URL & related info
        return ResponseEntity.ok(shortUrlService.createShortUrl(shortUrlRequest, userDetails.getUsername()));
    }

    // Endpoint to delete a shortened URL
    @DeleteMapping("/urls/{shortCode}")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable String shortCode,  // Short code of the URL
            @AuthenticationPrincipal UserDetails userDetails) { // Injects authenticated user
        shortUrlService.deleteShortUrl(shortCode, userDetails.getUsername());
        // Returns response 204 No Content on successful delete
        return ResponseEntity.noContent().build();
    }
}