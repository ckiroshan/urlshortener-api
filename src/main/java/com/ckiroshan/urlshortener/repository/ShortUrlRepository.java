package com.ckiroshan.urlshortener.repository;

import com.ckiroshan.urlshortener.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    // Checks if shortCode already exists in DB
    boolean existsByShortCode(String shortCode);
}
