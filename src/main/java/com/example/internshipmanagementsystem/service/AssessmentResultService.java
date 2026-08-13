package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.AssessmentResultRequest;
import com.example.internshipmanagementsystem.dto.response.AssessmentResultResponse;
import java.util.List;

public interface AssessmentResultService {

  List<AssessmentResultResponse> getResults(String actorUsername);

  AssessmentResultResponse createResult(AssessmentResultRequest request, String actorUsername);

  AssessmentResultResponse updateResult(
      Integer resultId, AssessmentResultRequest request, String actorUsername);
}
