package com.ckiroshan.urlshortener.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter @Setter @Builder
public class AdminStatsResponse {
    // Response DTO representing admin-level statistics.
    private long totalUrls;
    private long totalClicks;
    private long activeUsers;
    private Map<String, Long> clicksByCountry;
    private Map<String, Long> deviceDistribution;
}