package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.RoundCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.RoundCriterionResponse;
import java.util.List;

public interface RoundCriterionService {

  List<RoundCriterionResponse> getRoundCriteria(Integer roundId);

  RoundCriterionResponse getRoundCriterion(Integer roundCriterionId);

  RoundCriterionResponse createRoundCriterion(RoundCriterionRequest request);

  RoundCriterionResponse updateRoundCriterion(
      Integer roundCriterionId, RoundCriterionRequest request);

  void deleteRoundCriterion(Integer roundCriterionId);
}
