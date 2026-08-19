package com.example.internshipmanagementsystem.dto.response;

public record StudentSummaryResponse(
    Integer studentId,
    String studentCode,
    String fullName,
    String className,
    String major,
    boolean isActive) {}
