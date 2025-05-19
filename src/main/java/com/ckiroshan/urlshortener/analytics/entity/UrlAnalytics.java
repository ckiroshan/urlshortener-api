package com.ckiroshan.urlshortener.analytics.entity;

import com.ckiroshan.urlshortener.entity.ShortUrl;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_analytics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UrlAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generated primary key
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) // Many analytics entries can belong to one ShortUrl
    @JoinColumn(name = "short_url_id") // Foreign key to ShortUrl
    private ShortUrl shortUrl;
    @Column(nullable = false)
    private LocalDateTime accessedAt; // Timestamp of when the short URL was accessed
    private String referrer;    // Referring website or source
    private String userAgent;   // Browser or client info
    private String ipAddress;   // IP address of the user
    private String country;     // Country of the user
    private String deviceType;  // Type of device (mobile, tablet, desktop)
}