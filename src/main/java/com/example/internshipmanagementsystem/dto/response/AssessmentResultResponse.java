package com.example.internshipmanagementsystem.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AssessmentResultResponse(
    Long resultId,
    Long assignmentId,
    Long roundId,
    Long criterionId,
    BigDecimal score,
    String comments,
    Long evaluatedBy,
    LocalDateTime evaluationDate) {}
