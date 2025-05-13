package com.ckiroshan.urlshortener.controller;

import com.ckiroshan.urlshortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class RedirectController {
    private final ShortUrlService shortUrlService;

    @GetMapping("/{shortCode}")
    // Handles GET requests to short URLs & redirects to original URL
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = shortUrlService.getOriginalUrl(shortCode);
        // Responds with 302 FOUND and redirects to the original URL
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
