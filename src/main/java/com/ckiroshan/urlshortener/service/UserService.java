package com.ckiroshan.urlshortener.service;

import com.ckiroshan.urlshortener.dto.admin.UserResponse;

import java.util.List;

public interface UserService {
    // Returns list of all registered users
    List<UserResponse> getAllUsers();
    // Toggles user's active status (enabled/disabled) by ID
    UserResponse toggleUserStatus(Long userId);
}