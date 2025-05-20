package com.ckiroshan.urlshortener.service;

import com.ckiroshan.urlshortener.analytics.dto.AnalyticsResponse;
import com.ckiroshan.urlshortener.dto.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.ShortUrlResponse;

public interface ShortUrlService {
    // Creates a shortened URL from given request data
    ShortUrlResponse createShortUrl(ShortUrlRequest shortUrlRequest, String userEmail);
    // Retrieves original URL associated with given short code
    String getOriginalUrl(String shortCode);
    // Deletes an existing short code
    void deleteShortUrl(String shortCode);
    // Retrieves analytics data for a given short code & user's email
    AnalyticsResponse getAnalytics(String shortcode, String userEmail);
}
