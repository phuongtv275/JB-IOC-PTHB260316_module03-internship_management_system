package com.example.internshipmanagementsystem.controller;

import com.example.internshipmanagementsystem.dto.request.AssessmentRoundRequest;
import com.example.internshipmanagementsystem.dto.response.ApiResponse;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.service.AssessmentRoundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assessment_rounds")
@RequiredArgsConstructor
public class AssessmentRoundController {

  private final AssessmentRoundService assessmentRoundService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<PageResponse<AssessmentRoundResponse>>> getRounds(
      @RequestParam(name = "phase_id", required = false) Integer phaseId,
      @PageableDefault(size = 5) Pageable pageable) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Assessment rounds retrieved",
            PageResponse.from(assessmentRoundService.getRounds(phaseId), pageable)));
  }

  @GetMapping("/{roundId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<AssessmentRoundResponse>> getRound(
      @PathVariable Integer roundId) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200, "Assessment round retrieved", assessmentRoundService.getRound(roundId)));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<AssessmentRoundResponse>> createRound(
      @Valid @RequestBody AssessmentRoundRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.success(
                201, "Assessment round created", assessmentRoundService.createRound(request)));
  }

  @PutMapping("/{roundId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<AssessmentRoundResponse>> updateRound(
      @PathVariable Integer roundId, @Valid @RequestBody AssessmentRoundRequest request) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200, "Assessment round updated", assessmentRoundService.updateRound(roundId, request)));
  }

  @DeleteMapping("/{roundId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteRound(@PathVariable Integer roundId) {
    assessmentRoundService.deleteRound(roundId);
    return ResponseEntity.noContent().build();
  }
}
