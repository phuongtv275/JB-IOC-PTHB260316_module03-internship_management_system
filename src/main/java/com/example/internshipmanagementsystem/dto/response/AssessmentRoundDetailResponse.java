package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDate;
import java.util.List;

public record AssessmentRoundDetailResponse(
    Integer roundId,
    String roundName,
    LocalDate startDate,
    LocalDate endDate,
    String description,
    boolean isActive,
    PhaseReference phase,
    List<RoundCriterionDetailResponse> criteria) {}
