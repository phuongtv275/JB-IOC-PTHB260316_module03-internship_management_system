package com.example.internshipmanagementsystem.dto.response;

import java.math.BigDecimal;

public record EvaluationCriterionResponse(
    Long criterionId, String criterionName, String description, BigDecimal maxScore) {}
