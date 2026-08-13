package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AssessmentResultRequest(
    @NotNull Long assignmentId,
    @NotNull Long roundId,
    @NotNull Long criterionId,
    @NotNull @DecimalMin(value = "0") BigDecimal score,
    String comments) {}
