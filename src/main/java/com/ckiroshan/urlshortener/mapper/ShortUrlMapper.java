package com.ckiroshan.urlshortener.mapper;

import com.ckiroshan.urlshortener.dto.shorturl.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.shorturl.ShortUrlResponse;
import com.ckiroshan.urlshortener.entity.ShortUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShortUrlMapper {
    @Value("${app.base-url}")
    private String baseUrl; // Injects base URL to shortened links

    // Converts a ShortUrl entity to a ShortUrlResponse DTO
    public ShortUrlResponse entityToDto(ShortUrl shortUrl) {
        return ShortUrlResponse.builder()
                .shortUrl(baseUrl + "/" + shortUrl.getShortCode())
                .originalUrl(shortUrl.getOriginalUrl())
                .createdAt(shortUrl.getCreatedAt())
                .build();
    }

    // Converts a ShortUrlRequest DTO to a ShortUrl entity
    public ShortUrl dtoToEntity(ShortUrlRequest shortUrlRequest) {
        return ShortUrl.builder()
                .originalUrl(shortUrlRequest.getOriginalUrl())
                .shortCode(shortUrlRequest.getCustomShortCode() != null ?
                        shortUrlRequest.getCustomShortCode() :
                        generateRandomCode())
                .build();
    }

    // Generates a random 6-character string using UUID
    private String generateRandomCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
