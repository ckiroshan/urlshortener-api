package com.ckiroshan.urlshortener.service.impl;

import com.ckiroshan.urlshortener.dto.admin.UserResponse;
import com.ckiroshan.urlshortener.entity.User;
import com.ckiroshan.urlshortener.exception.ResourceNotFoundException;
import com.ckiroshan.urlshortener.mapper.UserMapper;
import com.ckiroshan.urlshortener.repository.UserRepository;
import com.ckiroshan.urlshortener.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        // Fetches all users & maps them to response DTOs
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse toggleUserStatus(Long userId) {
        // Retrieves user by ID & toggles their active status
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(!user.isActive());
        return userMapper.toResponseDto(userRepository.save(user));
    }
}