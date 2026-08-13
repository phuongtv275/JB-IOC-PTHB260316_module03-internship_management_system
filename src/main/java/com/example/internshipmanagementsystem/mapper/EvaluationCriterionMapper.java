package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.EvaluationCriterionResponse;
import com.example.internshipmanagementsystem.entity.EvaluationCriterion;
import org.springframework.stereotype.Component;

@Component
public class EvaluationCriterionMapper {

  public EvaluationCriterionResponse toResponse(EvaluationCriterion criterion) {
    return new EvaluationCriterionResponse(
        criterion.getCriterionId(),
        criterion.getCriterionName(),
        criterion.getDescription(),
        criterion.getMaxScore());
  }
}
