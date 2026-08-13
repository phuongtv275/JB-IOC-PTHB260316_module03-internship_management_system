package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.AssessmentResultResponse;
import com.example.internshipmanagementsystem.entity.AssessmentResult;
import org.springframework.stereotype.Component;

@Component
public class AssessmentResultMapper {

  public AssessmentResultResponse toResponse(AssessmentResult result) {
    return new AssessmentResultResponse(
        result.getResultId(),
        result.getAssignment().getAssignmentId(),
        result.getRound().getRoundId(),
        result.getCriterion().getCriterionId(),
        result.getScore(),
        result.getComments(),
        result.getEvaluatedBy().getUserId(),
        result.getEvaluationDate());
  }
}
