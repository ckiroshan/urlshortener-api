package com.ckiroshan.urlshortener.service.impl;

import com.ckiroshan.urlshortener.dto.admin.AdminStatsResponse;
import com.ckiroshan.urlshortener.dto.admin.AdminUrlResponse;
import com.ckiroshan.urlshortener.dto.analytics.AnalyticsResponse;
import com.ckiroshan.urlshortener.entity.UrlAnalytics;
import com.ckiroshan.urlshortener.exception.ForbiddenException;
import com.ckiroshan.urlshortener.mapper.AnalyticsMapper;
import com.ckiroshan.urlshortener.repository.UrlAnalyticsRepository;
import com.ckiroshan.urlshortener.dto.shorturl.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.shorturl.ShortUrlResponse;
import com.ckiroshan.urlshortener.entity.ShortUrl;
import com.ckiroshan.urlshortener.exception.BadRequestException;
import com.ckiroshan.urlshortener.exception.ResourceNotFoundException;
import com.ckiroshan.urlshortener.mapper.ShortUrlMapper;
import com.ckiroshan.urlshortener.repository.ShortUrlRepository;
import com.ckiroshan.urlshortener.service.ShortUrlService;
import com.ckiroshan.urlshortener.entity.User;
import com.ckiroshan.urlshortener.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void deleteShortUrl(String shortCode, @Nullable String userEmail) {
        // Find short URL entity
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                // Throw 404 if not found
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
        if (userEmail != null &&
        (shortUrl.getUser() == null ||
                !shortUrl.getUser().getEmail().equals(userEmail))) {
            // Throw 403 if not Auth User or Admin
            throw new ForbiddenException("You don't have permission to delete this URL");
        }
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

    // Admin related logic ===>
    @Override
    public List<AdminUrlResponse> getAllUrlsForAdmin() {
        // Returns all shortened URLs with admin-level details
        return shortUrlRepository.findAll().stream()
                .map(shortUrlMapper::toAdminUrlResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AdminStatsResponse getSystemStats() {
        // Gathers system-wide statistics for admin dashboard
        long totalUrls = shortUrlRepository.count();
        long totalClicks = shortUrlRepository.totalClicks();
        long activeUsers = userRepository.countByActive(true);

        // Handle country stats
        Map<String, Long> countryStats = analyticsRepository.countClicksByCountry()
                .stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));

        // Handle device stats
        Map<String, Long> deviceStats = analyticsRepository.countClicksByDeviceType()
                .stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));

        return AdminStatsResponse.builder()
                .totalUrls(totalUrls)
                .totalClicks(totalClicks)
                .activeUsers(activeUsers)
                .clicksByCountry(countryStats)
                .deviceDistribution(deviceStats)
                .build();
    }
}