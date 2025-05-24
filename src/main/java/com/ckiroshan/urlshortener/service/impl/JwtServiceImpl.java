package com.ckiroshan.urlshortener.service.impl;

import com.ckiroshan.urlshortener.entity.User;
import com.ckiroshan.urlshortener.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${app.security.jwt.secret}")
    private String secretKey; // Secret key for signing JWT
    @Value("${app.security.jwt.expiration}")
    private long jwtExpiration; // Token expiration duration

    @Override
    // Generates a JWT token with user's email and role
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail()) // Sets 'sub' claim as user's email
                .claim("role", user.getRole().name()) // Custom claim for user role
                .setIssuedAt(new Date()) // Current timestamp
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Expiration time
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Sign using HMAC SHA-256
                .compact(); // Generate the final token string
    }

    // Returns the key used to sign and validate JWTs
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decode base64 secret
        return Keys.hmacShaKeyFor(keyBytes); // Create signing key
    }

    @Override
    // Extracts username (email) from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    // Generic method to extract any claim using a resolver function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Parses all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Use same signing key
                .build()
                .parseClaimsJws(token) // Parse token
                .getBody(); // Return body (claims)
    }

    @Override
    // Checks if token is valid for the given user
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Get email
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Checks if the token is expired
    private boolean isTokenExpired(String token) {
        // Compare expiration time with current time
        return extractExpiration(token).before(new Date());
    }

    // Extracts the expiration date from token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}