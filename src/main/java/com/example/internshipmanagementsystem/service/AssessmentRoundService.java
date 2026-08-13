package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.AssessmentRoundRequest;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundResponse;
import java.util.List;

public interface AssessmentRoundService {

  List<AssessmentRoundResponse> getRounds(Long phaseId);

  AssessmentRoundResponse getRound(Long roundId);

  AssessmentRoundResponse createRound(AssessmentRoundRequest request);

  AssessmentRoundResponse updateRound(Long roundId, AssessmentRoundRequest request);

  void deleteRound(Long roundId);
}
