package com.ckiroshan.urlshortener.user.repository;

import com.ckiroshan.urlshortener.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Check if a user exists with the given email
    boolean existsByEmail(String email);
}