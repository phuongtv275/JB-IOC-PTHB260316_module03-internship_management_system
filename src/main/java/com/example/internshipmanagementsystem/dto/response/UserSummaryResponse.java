package com.example.internshipmanagementsystem.dto.response;

import com.example.internshipmanagementsystem.entity.Role;

public record UserSummaryResponse(
    Integer id, String username, String fullName, Role role, boolean isActive) {}
