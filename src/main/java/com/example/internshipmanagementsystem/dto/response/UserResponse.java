package com.example.internshipmanagementsystem.dto.response;

import com.example.internshipmanagementsystem.entity.Role;
import java.time.LocalDateTime;

public record UserResponse(
    Long userId,
    String username,
    String fullName,
    String email,
    String phoneNumber,
    Role role,
    boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
