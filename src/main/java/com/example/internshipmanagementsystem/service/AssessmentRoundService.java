package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.AssessmentRoundRequest;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundResponse;
import java.util.List;

public interface AssessmentRoundService {

  List<AssessmentRoundResponse> getRounds(Integer phaseId);

  AssessmentRoundResponse getRound(Integer roundId);

  AssessmentRoundResponse createRound(AssessmentRoundRequest request);

  AssessmentRoundResponse updateRound(Integer roundId, AssessmentRoundRequest request);

  void deleteRound(Integer roundId);
}
