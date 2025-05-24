package com.ckiroshan.urlshortener.dto.shorturl;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShortUrlResponse {
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime createdAt;
}
