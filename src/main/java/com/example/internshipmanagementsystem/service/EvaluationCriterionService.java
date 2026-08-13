package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.EvaluationCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.EvaluationCriterionResponse;
import java.util.List;

public interface EvaluationCriterionService {

  List<EvaluationCriterionResponse> getCriteria();

  EvaluationCriterionResponse getCriterion(Long criterionId);

  EvaluationCriterionResponse createCriterion(EvaluationCriterionRequest request);

  EvaluationCriterionResponse updateCriterion(Long criterionId, EvaluationCriterionRequest request);

  void deleteCriterion(Long criterionId);
}
