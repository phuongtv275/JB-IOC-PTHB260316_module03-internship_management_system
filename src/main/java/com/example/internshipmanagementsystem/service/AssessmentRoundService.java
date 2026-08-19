package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.AssessmentRoundRequest;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundDetailResponse;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AssessmentRoundService {

  PageResponse<AssessmentRoundSummaryResponse> getRounds(Integer phaseId, Pageable pageable);

  AssessmentRoundDetailResponse getRound(Integer roundId);

  AssessmentRoundDetailResponse createRound(AssessmentRoundRequest request);

  AssessmentRoundDetailResponse updateRound(Integer roundId, AssessmentRoundRequest request);

  void deleteRound(Integer roundId);
}
