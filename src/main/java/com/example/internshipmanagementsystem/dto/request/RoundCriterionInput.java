package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record RoundCriterionInput(
    @NotNull Long criterionId,
    @NotNull @DecimalMin(value = "0", inclusive = false) BigDecimal weight) {}
