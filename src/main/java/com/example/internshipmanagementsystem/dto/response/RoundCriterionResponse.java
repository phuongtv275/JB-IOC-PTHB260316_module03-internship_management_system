package com.example.internshipmanagementsystem.dto.response;

import java.math.BigDecimal;

public record RoundCriterionResponse(
    Long roundCriterionId, Long roundId, Long criterionId, BigDecimal weight) {}
