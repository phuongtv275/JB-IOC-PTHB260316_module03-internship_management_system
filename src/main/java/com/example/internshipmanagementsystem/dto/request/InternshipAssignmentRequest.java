package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record InternshipAssignmentRequest(
    @NotNull Integer studentId, @NotNull Integer mentorId, @NotNull Integer phaseId) {}
