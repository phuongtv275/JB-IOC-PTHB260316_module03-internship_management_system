package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AssessmentResultRequest(
    @NotNull Integer assignmentId,
    @NotNull Integer roundId,
    @NotNull Integer criterionId,
    @NotNull @DecimalMin(value = "0") BigDecimal score,
    String comments) {}
