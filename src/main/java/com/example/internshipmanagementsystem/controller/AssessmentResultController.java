package com.example.internshipmanagementsystem.controller;

import com.example.internshipmanagementsystem.dto.request.AssessmentResultRequest;
import com.example.internshipmanagementsystem.dto.response.ApiResponse;
import com.example.internshipmanagementsystem.dto.response.AssessmentResultResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.service.AssessmentResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assessment_results")
@RequiredArgsConstructor
public class AssessmentResultController {

  private final AssessmentResultService assessmentResultService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<PageResponse<AssessmentResultResponse>>> getResults(
      @AuthenticationPrincipal UserDetails userDetails,
      @PageableDefault(size = 5, sort = "resultId") Pageable pageable) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Assessment results retrieved",
            assessmentResultService.getResults(userDetails.getUsername(), pageable)));
  }

  @PostMapping
  @PreAuthorize("hasRole('MENTOR')")
  public ResponseEntity<ApiResponse<AssessmentResultResponse>> createResult(
      @Valid @RequestBody AssessmentResultRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.success(
                201,
                "Assessment result created",
                assessmentResultService.createResult(request, userDetails.getUsername())));
  }

  @PutMapping("/{resultId}")
  @PreAuthorize("hasRole('MENTOR')")
  public ResponseEntity<ApiResponse<AssessmentResultResponse>> updateResult(
      @PathVariable Integer resultId,
      @Valid @RequestBody AssessmentResultRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Assessment result updated",
            assessmentResultService.updateResult(resultId, request, userDetails.getUsername())));
  }
}
