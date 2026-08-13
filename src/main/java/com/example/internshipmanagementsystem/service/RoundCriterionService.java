package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.RoundCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.RoundCriterionResponse;
import java.util.List;

public interface RoundCriterionService {

  List<RoundCriterionResponse> getRoundCriteria(Long roundId);

  RoundCriterionResponse getRoundCriterion(Long roundCriterionId);

  RoundCriterionResponse createRoundCriterion(RoundCriterionRequest request);

  RoundCriterionResponse updateRoundCriterion(Long roundCriterionId, RoundCriterionRequest request);

  void deleteRoundCriterion(Long roundCriterionId);
}
