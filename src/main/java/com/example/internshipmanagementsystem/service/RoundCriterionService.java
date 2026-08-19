package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.RoundCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.RoundCriterionDetailResponse;
import org.springframework.data.domain.Pageable;

public interface RoundCriterionService {

  PageResponse<RoundCriterionDetailResponse> getRoundCriteria(Integer roundId, Pageable pageable);

  RoundCriterionDetailResponse getRoundCriterion(Integer roundCriterionId);

  RoundCriterionDetailResponse createRoundCriterion(RoundCriterionRequest request);

  RoundCriterionDetailResponse updateRoundCriterion(
      Integer roundCriterionId, RoundCriterionRequest request);

  void deleteRoundCriterion(Integer roundCriterionId);
}
