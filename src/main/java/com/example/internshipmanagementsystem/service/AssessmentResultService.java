package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.AssessmentResultRequest;
import com.example.internshipmanagementsystem.dto.response.AssessmentResultResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AssessmentResultService {

  PageResponse<AssessmentResultResponse> getResults(String actorUsername, Pageable pageable);

  AssessmentResultResponse createResult(AssessmentResultRequest request, String actorUsername);

  AssessmentResultResponse updateResult(
      Integer resultId, AssessmentResultRequest request, String actorUsername);
}
