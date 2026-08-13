package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MentorProfileRequest(
    @NotNull Integer mentorId,
    @Size(max = 100) String department,
    @Size(max = 50) String academicRank) {}
