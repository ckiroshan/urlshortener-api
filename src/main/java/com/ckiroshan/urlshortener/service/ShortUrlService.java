package com.ckiroshan.urlshortener.service;

import com.ckiroshan.urlshortener.dto.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.ShortUrlResponse;
import com.ckiroshan.urlshortener.user.entity.User;

public interface ShortUrlService {
    // Creates a shortened URL from given request data
    ShortUrlResponse createShortUrl(ShortUrlRequest shortUrlRequest, User user);
    // Retrieves original URL associated with given short code
    String getOriginalUrl(String shortCode);
    // Deletes an existing short code
    void deleteShortUrl(String shortCode);
}
