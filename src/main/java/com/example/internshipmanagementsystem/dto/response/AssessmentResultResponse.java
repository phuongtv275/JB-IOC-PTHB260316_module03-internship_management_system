package com.example.internshipmanagementsystem.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AssessmentResultResponse(
    Integer resultId,
    InternshipAssignmentSummaryResponse assignment,
    RoundReference round,
    CriterionReference criterion,
    BigDecimal score,
    String comments,
    UserSummaryResponse evaluator,
    LocalDateTime evaluationDate) {}
