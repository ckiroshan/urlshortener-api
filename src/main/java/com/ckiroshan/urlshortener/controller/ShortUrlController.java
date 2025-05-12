package com.ckiroshan.urlshortener.controller;

import com.ckiroshan.urlshortener.dto.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.ShortUrlResponse;
import com.ckiroshan.urlshortener.service.ShortUrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shorten") // Base path for all URL shortening endpoints
@RequiredArgsConstructor // Generates constructor for final fields
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    @PostMapping // Endpoint to create a shortened URL
    public ResponseEntity<ShortUrlResponse> createShortUrl(
            @Valid @RequestBody ShortUrlRequest shortUrlRequest) {
        // Returns a response containing the shortened URL & related info
        return ResponseEntity.ok(shortUrlService.createShortUrl(shortUrlRequest));
    }
}
