package com.ckiroshan.urlshortener.repository;

import com.ckiroshan.urlshortener.entity.ShortUrl;
import com.ckiroshan.urlshortener.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    // Checks if shortCode already exists in DB
    boolean existsByShortCode(String shortCode);
    // Retrieves a ShortUrl entity by its short code, if present
    Optional<ShortUrl> findByShortCode(String shortCode);
    // Retrieves a ShortUrl by short code & user
    Optional<ShortUrl> findByShortCodeAndUser(String shortCode, User user);
}
