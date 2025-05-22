package com.ckiroshan.urlshortener.ratelimiting.service;

import com.ckiroshan.urlshortener.ratelimiting.config.RateLimitingConfig;
import jakarta.servlet.http.HttpServletRequest;

public interface RateLimitingService {
    // Check if request is allowed based on rate limits
    boolean allowRequest(HttpServletRequest request, String username);
    // Return rate limiting configuration
    RateLimitingConfig getConfig();
    // Return remaining tokens for identifier (IP || username)
    int getRemainingTokens(String identifier);
    // Return time in seconds until next token refill
    String getResetTime(String identifier);
}