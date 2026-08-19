package com.example.internshipmanagementsystem.dto.response;

import java.math.BigDecimal;

public record CriterionReference(
    Integer id, String name, String description, BigDecimal maxScore) {}
