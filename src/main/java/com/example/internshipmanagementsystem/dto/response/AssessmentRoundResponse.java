package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDate;

public record AssessmentRoundResponse(
    Long roundId,
    Long phaseId,
    String roundName,
    LocalDate startDate,
    LocalDate endDate,
    String description,
    boolean isActive) {}
