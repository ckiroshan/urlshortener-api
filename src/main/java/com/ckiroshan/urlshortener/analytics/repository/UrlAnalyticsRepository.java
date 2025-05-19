package com.ckiroshan.urlshortener.analytics.repository;

import com.ckiroshan.urlshortener.analytics.entity.UrlAnalytics;
import com.ckiroshan.urlshortener.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics, Long> {
    // Retrieves all analytics entries for a given short URL
    List<UrlAnalytics> findByShortUrl(ShortUrl shortUrl);
}