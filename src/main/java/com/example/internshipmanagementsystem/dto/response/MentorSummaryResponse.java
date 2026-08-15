package com.example.internshipmanagementsystem.dto.response;

public record MentorSummaryResponse(
    Integer mentorId, String fullName, String department, String academicRank, boolean isActive) {}
