package com.example.internshipmanagementsystem.dto.response;

import java.math.BigDecimal;

public record EvaluationCriterionResponse(
    Integer criterionId, String criterionName, String description, BigDecimal maxScore) {}
