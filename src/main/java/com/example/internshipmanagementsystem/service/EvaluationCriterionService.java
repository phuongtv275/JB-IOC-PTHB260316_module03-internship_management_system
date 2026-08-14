package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.EvaluationCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.EvaluationCriterionResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface EvaluationCriterionService {

  PageResponse<EvaluationCriterionResponse> getCriteria(Pageable pageable);

  EvaluationCriterionResponse getCriterion(Integer criterionId);

  EvaluationCriterionResponse createCriterion(EvaluationCriterionRequest request);

  EvaluationCriterionResponse updateCriterion(
      Integer criterionId, EvaluationCriterionRequest request);

  void deleteCriterion(Integer criterionId);
}
