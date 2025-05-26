package com.ckiroshan.urlshortener.repository;

import com.ckiroshan.urlshortener.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Check if a user exists with the given email
    boolean existsByEmail(String email);
    // Retrieve a user by email (used for login)
    Optional<User> findByEmail(String email);
    // Count users by active status
    long countByActive(@Param("active") boolean active);
}