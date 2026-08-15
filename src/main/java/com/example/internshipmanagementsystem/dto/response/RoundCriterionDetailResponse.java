package com.example.internshipmanagementsystem.dto.response;

import java.math.BigDecimal;

public record RoundCriterionDetailResponse(
    Integer roundCriterionId,
    RoundReference round,
    CriterionReference criterion,
    BigDecimal weight) {}
