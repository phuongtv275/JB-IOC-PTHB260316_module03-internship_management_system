package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.EvaluationCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.EvaluationCriterionResponse;
import java.util.List;

public interface EvaluationCriterionService {

  List<EvaluationCriterionResponse> getCriteria();

  EvaluationCriterionResponse getCriterion(Integer criterionId);

  EvaluationCriterionResponse createCriterion(EvaluationCriterionRequest request);

  EvaluationCriterionResponse updateCriterion(
      Integer criterionId, EvaluationCriterionRequest request);

  void deleteCriterion(Integer criterionId);
}
