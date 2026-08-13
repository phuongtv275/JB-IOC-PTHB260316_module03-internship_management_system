package com.example.internshipmanagementsystem.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AssessmentResultResponse(
    Integer resultId,
    Integer assignmentId,
    Integer roundId,
    Integer criterionId,
    BigDecimal score,
    String comments,
    Integer evaluatedBy,
    LocalDateTime evaluationDate) {}
