package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StudentResponse(
    Long studentId,
    String studentCode,
    String major,
    String className,
    LocalDate dateOfBirth,
    String address,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
