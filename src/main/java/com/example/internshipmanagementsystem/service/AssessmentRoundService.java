package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.AssessmentRoundRequest;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AssessmentRoundService {

  PageResponse<AssessmentRoundResponse> getRounds(Integer phaseId, Pageable pageable);

  AssessmentRoundResponse getRound(Integer roundId);

  AssessmentRoundResponse createRound(AssessmentRoundRequest request);

  AssessmentRoundResponse updateRound(Integer roundId, AssessmentRoundRequest request);

  void deleteRound(Integer roundId);
}
