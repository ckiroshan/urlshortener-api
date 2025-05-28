package com.ckiroshan.urlshortener.controller;

import com.ckiroshan.urlshortener.dto.admin.AdminStatsResponse;
import com.ckiroshan.urlshortener.dto.admin.AdminUrlResponse;
import com.ckiroshan.urlshortener.dto.admin.UserResponse;
import com.ckiroshan.urlshortener.service.ShortUrlService;
import com.ckiroshan.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final ShortUrlService shortUrlService;

    // Fetch all registered users from DB
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Enable/Disable user based on User ID
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<UserResponse> toggleUserStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.toggleUserStatus(userId));
    }

    // Fetch all short urls created from DB
    @GetMapping("/urls")
    public ResponseEntity<List<AdminUrlResponse>> getAllUrls() {
        return ResponseEntity.ok(shortUrlService.getAllUrlsForAdmin());
    }

    // Delete an existing short url from DB
    @DeleteMapping("/urls/{shortCode}")
    public ResponseEntity<Void> adminDeleteUrl(@PathVariable String shortCode) {
        shortUrlService.deleteShortUrl(shortCode, null); // null indicates admin request
        return ResponseEntity.noContent().build();
    }

    // Fetch all system stats for Admin
    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getSystemStats() {
        return ResponseEntity.ok(shortUrlService.getSystemStats());
    }
}