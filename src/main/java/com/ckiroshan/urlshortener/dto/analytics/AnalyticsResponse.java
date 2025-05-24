package com.ckiroshan.urlshortener.dto.analytics;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnalyticsResponse {
    private String shortCode;
    private String originalUrl;
    private long totalClicks; // Total number of clicks for this short URL
    private List<ClickData> clicks; // Detailed data for each click

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ClickData{
        private LocalDateTime timestamp;  // Time when the URL was accessed
        private String referrer;         // Referring website
        private String country;         // Visitor's country
        private String deviceType;     // Device used to access
    }
}