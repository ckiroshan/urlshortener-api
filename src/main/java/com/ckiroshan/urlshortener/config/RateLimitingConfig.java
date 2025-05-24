package com.ckiroshan.urlshortener.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.rate-limiting")
@Getter @Setter
public class RateLimitingConfig {
    private boolean enabled; // If rate limiting is enabled
    private int capacity; // Max allowed requests in bucket
    private int refillPerMinute; // Number of tokens per minute
    private boolean byIp; // Rate limit by IP
    private boolean byUser;  // Rate limit by Auth User
}
