package com.ckiroshan.urlshortener.repository;

import com.ckiroshan.urlshortener.entity.ShortUrl;
import com.ckiroshan.urlshortener.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    // Checks if shortCode already exists in DB
    boolean existsByShortCode(String shortCode);
    // Retrieves a ShortUrl entity by its short code, if present
    Optional<ShortUrl> findByShortCode(String shortCode);
    // Retrieves a ShortUrl by short code & user
    Optional<ShortUrl> findByShortCodeAndUser(String shortCode, User user);
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"user"})
    // Fetch all ShortUrls with associated user eagerly loaded
    List<ShortUrl> findAll();
    // Total number of clicks across all short URLs
    @Query("SELECT COALESCE(SUM(s.clickCount), 0) FROM ShortUrl s")
    long totalClicks();
}
