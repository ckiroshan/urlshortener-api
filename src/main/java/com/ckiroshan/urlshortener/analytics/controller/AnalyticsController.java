package com.ckiroshan.urlshortener.analytics.controller;

import com.ckiroshan.urlshortener.analytics.dto.AnalyticsResponse;
import com.ckiroshan.urlshortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics") // Base path
@RequiredArgsConstructor
public class AnalyticsController {
    private final ShortUrlService shortUrlService;

    @GetMapping("/{shortCode}") // Endpoint to get analytics for short code
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @PathVariable String shortCode, // Short code to track
            @AuthenticationPrincipal UserDetails userDetails) { // Authenticated user info
        // Retrieves analytics from service and return the response
        return ResponseEntity.ok(shortUrlService
                .getAnalytics(shortCode, userDetails.getUsername()));
    }
}