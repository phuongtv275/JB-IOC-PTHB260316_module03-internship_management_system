package com.example.internshipmanagementsystem.controller;

import com.example.internshipmanagementsystem.dto.request.EvaluationCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.ApiResponse;
import com.example.internshipmanagementsystem.dto.response.EvaluationCriterionResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.service.EvaluationCriterionService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evaluation_criteria")
@RequiredArgsConstructor
public class EvaluationCriterionController {

  private final EvaluationCriterionService evaluationCriterionService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<PageResponse<EvaluationCriterionResponse>>> getCriteria(
      @PageableDefault(size = 5) Pageable pageable) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Evaluation criteria retrieved",
            evaluationCriterionService.getCriteria(pageable)));
  }

  @GetMapping("/{criterionId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<EvaluationCriterionResponse>> getCriterion(
      @PathVariable Integer criterionId) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Evaluation criterion retrieved",
            evaluationCriterionService.getCriterion(criterionId)));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<EvaluationCriterionResponse>> createCriterion(
      @Valid @RequestBody EvaluationCriterionRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.success(
                201,
                "Evaluation criterion created",
                evaluationCriterionService.createCriterion(request)));
  }

  @PutMapping("/{criterionId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<EvaluationCriterionResponse>> updateCriterion(
      @PathVariable Integer criterionId, @Valid @RequestBody EvaluationCriterionRequest request) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Evaluation criterion updated",
            evaluationCriterionService.updateCriterion(criterionId, request)));
  }

  @DeleteMapping("/{criterionId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteCriterion(@PathVariable Integer criterionId) {
    evaluationCriterionService.deleteCriterion(criterionId);
    return ResponseEntity.noContent().build();
  }
}
