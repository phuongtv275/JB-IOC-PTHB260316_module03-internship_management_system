package com.example.internshipmanagementsystem.dto.response;

import java.math.BigDecimal;

public record RoundCriterionResponse(
    Integer roundCriterionId, Integer roundId, Integer criterionId, BigDecimal weight) {}
