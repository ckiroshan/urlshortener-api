package com.ckiroshan.urlshortener.user.service;

import com.ckiroshan.urlshortener.user.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {
    // Generates a JWT token using user info
    String generateToken(User user);
    // Extracts the username (email) from the token
    String extractUsername(String token);
    // Extracts a specific claim from the token using a resolver function
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    // Validates the token against user details (username + expiration)
    boolean isTokenValid(String token, UserDetails userDetails);
}