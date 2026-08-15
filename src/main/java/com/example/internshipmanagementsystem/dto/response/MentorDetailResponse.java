package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDateTime;

public record MentorDetailResponse(
    Integer mentorId,
    String department,
    String academicRank,
    UserResponse account,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
