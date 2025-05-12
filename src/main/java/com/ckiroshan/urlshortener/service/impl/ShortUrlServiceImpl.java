package com.ckiroshan.urlshortener.service.impl;

import com.ckiroshan.urlshortener.dto.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.ShortUrlResponse;
import com.ckiroshan.urlshortener.entity.ShortUrl;
import com.ckiroshan.urlshortener.exception.BadRequestException;
import com.ckiroshan.urlshortener.exception.ResourceNotFoundException;
import com.ckiroshan.urlshortener.mapper.ShortUrlMapper;
import com.ckiroshan.urlshortener.repository.ShortUrlRepository;
import com.ckiroshan.urlshortener.service.ShortUrlService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Generates constructor with required (final) fields
public class ShortUrlServiceImpl implements ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final ShortUrlMapper shortUrlMapper;

    @Override
    @Transactional // Ensures atomic DB operations
    public ShortUrlResponse createShortUrl(ShortUrlRequest shortUrlRequest) {
        // Check if provided custom code already exists
        if (shortUrlRequest.getCustomShortCode() != null &&
                shortUrlRepository.existsByShortCode(shortUrlRequest.getCustomShortCode())) {
            throw new BadRequestException("Custom short code already exists");
        }
        // Convert request DTO to entity and persist it to DB
        ShortUrl shortUrl = shortUrlMapper.dtoToEntity(shortUrlRequest);
        // Convert saved entity back to response DTO
        return shortUrlMapper.entityToDto(shortUrlRepository.save(shortUrl));
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        // Look up the original URL by short code
        return shortUrlRepository.findByShortCode(shortCode)
                .map(ShortUrl::getOriginalUrl)
                // Throw 404 if not found
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));
    }
}
