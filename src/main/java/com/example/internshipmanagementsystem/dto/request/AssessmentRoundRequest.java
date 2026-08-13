package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record AssessmentRoundRequest(
    @NotNull Integer phaseId,
    @NotBlank @Size(max = 100) String roundName,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    String description,
    List<@Valid RoundCriterionInput> criteria) {}
