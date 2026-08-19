package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StudentDetailResponse(
    Integer studentId,
    String studentCode,
    String major,
    String className,
    LocalDate dateOfBirth,
    String address,
    UserResponse account,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
