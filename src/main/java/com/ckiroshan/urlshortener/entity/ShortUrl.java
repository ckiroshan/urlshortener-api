package com.ckiroshan.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 10)
    private String shortCode;
    @Column(unique = false, columnDefinition = "TEXT")
    private String originalUrl;
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();
}
