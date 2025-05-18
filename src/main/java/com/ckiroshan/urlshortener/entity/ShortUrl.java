package com.ckiroshan.urlshortener.entity;

import com.ckiroshan.urlshortener.user.entity.User;
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
    @ManyToOne(fetch = FetchType.LAZY) // Many ShortUrls can belong to one User
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column in the DB
    private User user;
}