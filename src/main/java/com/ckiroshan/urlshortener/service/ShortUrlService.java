package com.ckiroshan.urlshortener.service;

import com.ckiroshan.urlshortener.dto.ShortUrlRequest;
import com.ckiroshan.urlshortener.dto.ShortUrlResponse;

public interface ShortUrlService {
    // Creates a shortened URL from given request data
    ShortUrlResponse createShortUrl(ShortUrlRequest shortUrlRequest);
}
