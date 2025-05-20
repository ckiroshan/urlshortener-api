package com.ckiroshan.urlshortener.service.impl;

import com.ckiroshan.urlshortener.analytics.dto.AnalyticsResponse;
import com.ckiroshan.urlshortener.analytics.entity.UrlAnalytics;
import com.ckiroshan.urlshortener.analytics.mapper.AnalyticsMapper;
import com.ckiroshan.urlshortener.analytics.repository.UrlAnalyticsRepository;
import com.ckiroshan.urlshortener.dto.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.ShortUrlResponse;
import com.ckiroshan.urlshortener.entity.ShortUrl;
import com.ckiroshan.urlshortener.exception.BadRequestException;
import com.ckiroshan.urlshortener.exception.ResourceNotFoundException;
import com.ckiroshan.urlshortener.mapper.ShortUrlMapper;
import com.ckiroshan.urlshortener.repository.ShortUrlRepository;
import com.ckiroshan.urlshortener.service.ShortUrlService;
import com.ckiroshan.urlshortener.user.entity.User;
import com.ckiroshan.urlshortener.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor // Generates constructor with required (final) fields
public class ShortUrlServiceImpl implements ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final UrlAnalyticsRepository analyticsRepository;
    private final ShortUrlMapper shortUrlMapper;
    private final AnalyticsMapper analyticsMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional // Ensures atomic DB operations
    public ShortUrlResponse createShortUrl(ShortUrlRequest shortUrlRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BadRequestException("User not found"));
        // Check if provided custom code already exists
        if (shortUrlRequest.getCustomShortCode() != null &&
                shortUrlRepository.existsByShortCode(shortUrlRequest.getCustomShortCode())) {
            throw new BadRequestException("Custom short code already exists");
        }
        // Convert request DTO to entity and persist it to DB
        ShortUrl shortUrl = shortUrlMapper.dtoToEntity(shortUrlRequest);
        shortUrl.setUser(user); // Associate URL with the currently logged-in user
        // Convert saved entity back to response DTO
        return shortUrlMapper.entityToDto(shortUrlRepository.save(shortUrl));
    }

    @Override
    @Cacheable(value = "shortUrls", key = "#shortCode") // Caches original URL lookup by shortCode
    public String getOriginalUrl(String shortCode) {
        // Look up the original URL by short code
        return shortUrlRepository.findByShortCode(shortCode)
                .map(ShortUrl::getOriginalUrl)
                // Throw 404 if not found
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
    }

    @Override
    @Transactional
    @CacheEvict(value = "shortUrls", key = "#shortCode") // Evicts cached URL on deletion
    public void deleteShortUrl(String shortCode) {
        // Find short URL entity
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                // Throw 404 if not found
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
        // Delete the entity from DB
        shortUrlRepository.delete(shortUrl);
    }

    // Analytics related logic ===>
    @Override
    @Transactional
    public String getOriginalUrlWithTracking(String shortCode, HttpServletRequest request) {
        // Retrieves original URL & logs access analytics
        String originalUrl = this.getOriginalUrl(shortCode); // Gets the cached URL
        trackAccess(shortCode, request); // Records the access
        return originalUrl;
    }

    @Override
    @Transactional
    public void trackAccess(String shortCode, HttpServletRequest request) {
        // Increments click count & saves analytics data
        // Tracking implementation
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
        // Increment click count
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrlRepository.save(shortUrl);
        // Builds & saves analytics entry
        UrlAnalytics analytics = UrlAnalytics.builder()
                .shortUrl(shortUrl)
                .accessedAt(LocalDateTime.now())
                .ipAddress(getClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .referrer(getSanitizedReferrer(request))
                .country(detectCountry(request))
                .deviceType(detectDeviceType(request))
                .build();
        analyticsRepository.save(analytics);
    }

    // Retrieves client IP address from request headers
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        // Handle IPv6 localhost case
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    // Returns referer or marks as 'direct' if not available
    private String getSanitizedReferrer(HttpServletRequest request) {
        String referrer = request.getHeader("referer");
        return (referrer != null && !referrer.trim().isEmpty()) ? referrer : "direct";
    }

    // Placeholder for country detection (Will be implemented later)
    private String detectCountry(HttpServletRequest request) {
        // (Expect: To be Implemented using GeoIP or external service)
        return "Unknown";
    }

    // Detects device type based on User-Agent string
    private String detectDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) return "Unknown";
        if (userAgent.contains("Mobile")) return "Mobile";
        if (userAgent.contains("Tablet")) return "Tablet";
        return "Desktop";
    }

    @Override
    public AnalyticsResponse getAnalytics(String shortCode, String userEmail) {
        // Retrieves user & URL, verifies ownership, then returns analytics
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BadRequestException("User not found"));
        ShortUrl shortUrl = shortUrlRepository.findByShortCodeAndUser(shortCode, user)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found or unauthorized"));
        // Fetches all analytics records associated with given short URL
        List<UrlAnalytics> analytics = analyticsRepository.findByShortUrl(shortUrl);
        return analyticsMapper.toAnalyticsResponse(shortUrl, analytics);
    }
}