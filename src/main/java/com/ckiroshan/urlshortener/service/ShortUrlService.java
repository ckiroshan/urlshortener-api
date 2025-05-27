package com.ckiroshan.urlshortener.service;

import com.ckiroshan.urlshortener.dto.admin.AdminStatsResponse;
import com.ckiroshan.urlshortener.dto.admin.AdminUrlResponse;
import com.ckiroshan.urlshortener.dto.analytics.AnalyticsResponse;
import com.ckiroshan.urlshortener.dto.shorturl.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.shorturl.ShortUrlResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ShortUrlService {
    // Creates a shortened URL from given request data
    ShortUrlResponse createShortUrl(ShortUrlRequest shortUrlRequest, String userEmail);
    // Retrieves original URL associated with given short code
    String getOriginalUrl(String shortCode);
    // Deletes an existing short code
    void deleteShortUrl(String shortCode, String userEmail);
    // Retrieves analytics data for a given short code & user's email
    AnalyticsResponse getAnalytics(String shortcode, String userEmail);
    // Retrieves original URL & logs the access for tracking purposes
    String getOriginalUrlWithTracking(String shortCode, HttpServletRequest request);
    // Tracks access data (IP, referrer, device, etc.) for a short code
    void trackAccess(String shortCode, HttpServletRequest request);
    // Returns list of all shortened URLs for admin overview
    List<AdminUrlResponse> getAllUrlsForAdmin();
    // Returns system-wide stats: Total URLs, clicks, users, etc.
    AdminStatsResponse getSystemStats();
}
