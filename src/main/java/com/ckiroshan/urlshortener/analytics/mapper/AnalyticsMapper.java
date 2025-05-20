package com.ckiroshan.urlshortener.analytics.mapper;

import com.ckiroshan.urlshortener.analytics.dto.AnalyticsResponse;
import com.ckiroshan.urlshortener.analytics.entity.UrlAnalytics;
import com.ckiroshan.urlshortener.entity.ShortUrl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnalyticsMapper {
    // Converts ShortUrl and list of analytics data into a response DTO
    public AnalyticsResponse toAnalyticsResponse(ShortUrl shortUrl, List<UrlAnalytics> analytics) {
        return AnalyticsResponse.builder()
                .shortCode(shortUrl.getShortCode())
                .originalUrl(shortUrl.getOriginalUrl())
                .totalClicks(shortUrl.getClickCount())
                .clicks(analytics.stream()
                        .map(this::toClickData)
                        .collect(Collectors.toList()))
                .build();
    }

    // Converts individual UrlAnalytics entry to ClickData DTO
    public AnalyticsResponse.ClickData toClickData(UrlAnalytics analytics) {
        return AnalyticsResponse.ClickData.builder()
                .timestamp(analytics.getAccessedAt())
                .referrer(analytics.getReferrer())
                .country(analytics.getCountry())
                .deviceType(analytics.getDeviceType())
                .build();
    }
}