package com.ckiroshan.urlshortener.ratelimiting.service.impl;

import com.ckiroshan.urlshortener.ratelimiting.config.RateLimitingConfig;
import com.ckiroshan.urlshortener.ratelimiting.service.RateLimitingService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RateLimitingServiceImpl implements RateLimitingService {
    private static final int TOKENS_PER_REQUEST = 1;
    private static final Duration REFILL_DURATION = Duration.ofMinutes(1);
    // Configuration values loaded from application properties
    private final RateLimitingConfig config;
    // Buckets mapped by IP address
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    // Buckets mapped by username
    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();
    // Creation timestamps for every bucket
    private final Map<String, Long> bucketCreationTimes = new ConcurrentHashMap<>();

    // Check if request is allowed based on rate limits
    public boolean allowRequest(HttpServletRequest request, String username) {
        // If rate limiting is disabled, allow all requests
        if (!config.isEnabled()) return true;
        // Extract client IP
        String ip = getClientIp(request);
        // List of Buckets
        List<Bucket> buckets = new ArrayList<>();
        // Apply IP-based rate limiting if enabled
        if (config.isByIp()) {
            buckets.add(ipBuckets.computeIfAbsent(ip, k -> {
                Bucket newBucket = createNewBucket();
                bucketCreationTimes.putIfAbsent(k, System.currentTimeMillis()); // Record creation time
                return newBucket;
            }));
        }
        // Apply user-based rate limiting if enabled & user is authenticated
        if (config.isByUser() && username != null) {
            buckets.add(userBuckets.computeIfAbsent(username, k -> {
                Bucket newBucket = createNewBucket();
                bucketCreationTimes.putIfAbsent(k, System.currentTimeMillis()); // Record creation time
                return newBucket;
            }));
        }
        // Allow request only if all buckets allow consuming a token
        return buckets.isEmpty() || buckets.stream().allMatch(b -> b.tryConsume(TOKENS_PER_REQUEST));
    }

    @Override
    // Return remaining tokens for identifier (IP || username)
    public int getRemainingTokens(String identifier) {
        // First find bucket by IP, then by User
        Bucket bucket = ipBuckets.getOrDefault(identifier,
                userBuckets.getOrDefault(identifier, null));
        // If exists, return available tokens, else return full capacity
        return bucket != null ? (int) bucket.getAvailableTokens() : config.getCapacity();
    }

    // Create a new rate limit bucket with capacity, refill rate
    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(
                        Bandwidth.builder()
                                .capacity(config.getCapacity()) // Max tokens in bucket
                                .refillIntervally(config.getRefillPerMinute(), REFILL_DURATION)
                                .build())
                .build();
    }

    @Override
    // Returns current configuration instance
    public RateLimitingConfig getConfig() {
        return config;
    }

    @Override
    // Return time in seconds until next token refill
    public String getResetTime(String identifier) {
        // If rate limiting is off
        if (!config.isEnabled()) return String.valueOf(REFILL_DURATION.toSeconds());
        // Get creation time of bucket
        Long creationTime = bucketCreationTimes.get(identifier);
        if (creationTime == null) {
            // If no bucket exists, then reset after one full interval
            return String.valueOf(REFILL_DURATION.toSeconds());
        }

        long now = System.currentTimeMillis();
        long refillIntervalMs = REFILL_DURATION.toMillis();
        // Time since bucket creation
        long elapsedMs = now - creationTime;
        // Number of full refill intervals passed
        long intervalsPassed = elapsedMs / refillIntervalMs;
        // Timestamp of the next refill
        long nextRefillMs = creationTime + ((intervalsPassed + 1) * refillIntervalMs);
        // Remaining time until next refill
        long remainingMs = nextRefillMs - now;
        // If remainingMs is negative
        if (remainingMs < 0) {
            remainingMs = 0;
        }
        return String.valueOf(remainingMs / 1000); // Convert to seconds
    }

    // Extract client IP from header or request
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isEmpty()) ? request.getRemoteAddr() : ip;
    }
}