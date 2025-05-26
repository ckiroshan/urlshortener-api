package com.ckiroshan.urlshortener.repository;

import com.ckiroshan.urlshortener.entity.UrlAnalytics;
import com.ckiroshan.urlshortener.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics, Long> {
    // Retrieves all analytics entries for a given short URL
    List<UrlAnalytics> findByShortUrl(ShortUrl shortUrl);
    // Count clicks grouped by country
    @Query("SELECT COALESCE(a.country, 'Unknown') as country, COUNT(a) as count " +
            "FROM UrlAnalytics a GROUP BY COALESCE(a.country, 'Unknown')")
    List<Object[]> countClicksByCountry();
    // Count clicks grouped by device type
    @Query("SELECT COALESCE(a.deviceType, 'Unknown') as deviceType, COUNT(a) as count " +
            "FROM UrlAnalytics a GROUP BY COALESCE(a.deviceType, 'Unknown')")
    List<Object[]> countClicksByDeviceType();
}