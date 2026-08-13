package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDateTime;

public record MentorResponse(
    Long mentorId,
    String department,
    String academicRank,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
