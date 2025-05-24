package com.ckiroshan.urlshortener.service.impl;

import com.ckiroshan.urlshortener.exception.AuthenticationException;
import com.ckiroshan.urlshortener.exception.BadRequestException;
import com.ckiroshan.urlshortener.dto.user.AuthResponse;
import com.ckiroshan.urlshortener.dto.user.LoginRequest;
import com.ckiroshan.urlshortener.dto.user.RegisterRequest;
import com.ckiroshan.urlshortener.entity.User;
import com.ckiroshan.urlshortener.mapper.UserMapper;
import com.ckiroshan.urlshortener.repository.UserRepository;
import com.ckiroshan.urlshortener.service.AuthService;
import com.ckiroshan.urlshortener.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Injects final dependencies via constructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional // Ensures atomic DB operations during registration
    public AuthResponse register(RegisterRequest request) {
        // Checks if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        // Map DTO to User entity and encode password
        User user = userMapper.registerDTOtoEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Save new user to DB
        user = userRepository.save(user);
        // Generate JWT token & return auth response
        String jwtToken = jwtService.generateToken(user);
        return userMapper.authDTOtoAuthResponse(user, jwtToken);
    }


    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            // Fetch user by email
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
            // Check if password matches
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new AuthenticationException("Invalid credentials");
            }
            // Generate JWT token & return auth response
            String jwtToken = jwtService.generateToken(user);
            return userMapper.authDTOtoAuthResponse(user, jwtToken);
        } catch (UsernameNotFoundException | AuthenticationException ex) {
            // Throw error message for login failures
            throw new AuthenticationException("Invalid email or password");
        }
    }
}