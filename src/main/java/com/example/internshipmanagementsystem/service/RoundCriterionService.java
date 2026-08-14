package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.RoundCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.RoundCriterionResponse;
import org.springframework.data.domain.Pageable;

public interface RoundCriterionService {

  PageResponse<RoundCriterionResponse> getRoundCriteria(Integer roundId, Pageable pageable);

  RoundCriterionResponse getRoundCriterion(Integer roundCriterionId);

  RoundCriterionResponse createRoundCriterion(RoundCriterionRequest request);

  RoundCriterionResponse updateRoundCriterion(
      Integer roundCriterionId, RoundCriterionRequest request);

  void deleteRoundCriterion(Integer roundCriterionId);
}
