package com.ckiroshan.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increments the ID
    private Long id;
    @Column(unique = true, nullable = false, length = 10) // Unique short code
    private String shortCode;
    @Column(unique = false, columnDefinition = "TEXT") // Original long URL, stored as text
    private String originalUrl;
    @CreationTimestamp // Timestamp when the entity is created
    private LocalDateTime createdAt = LocalDateTime.now(); // Default value is local time
    @Column(nullable = false, columnDefinition = "integer default 0")
    private int clickCount = 0;  // Tracks click count (Default 0)
    @ManyToOne(fetch = FetchType.LAZY) // Many ShortUrls can belong to one User
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column in the DB
    private User user;
}