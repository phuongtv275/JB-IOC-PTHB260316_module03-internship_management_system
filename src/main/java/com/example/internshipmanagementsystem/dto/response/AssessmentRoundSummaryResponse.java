package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDate;

public record AssessmentRoundSummaryResponse(
    Integer roundId,
    String roundName,
    LocalDate startDate,
    LocalDate endDate,
    boolean isActive,
    long criteriaCount,
    PhaseReference phase) {}
