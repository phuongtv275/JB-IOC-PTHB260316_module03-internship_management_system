package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record InternshipAssignmentRequest(
    @NotNull Long studentId, @NotNull Long mentorId, @NotNull Long phaseId) {}
