package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record EvaluationCriterionRequest(
    @NotBlank @Size(max = 200) String criterionName,
    String description,
    @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 3, fraction = 2)
        BigDecimal maxScore) {}
