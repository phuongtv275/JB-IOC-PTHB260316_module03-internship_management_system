package com.example.internshipmanagementsystem.controller;

import com.example.internshipmanagementsystem.dto.request.RoundCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.ApiResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.RoundCriterionResponse;
import com.example.internshipmanagementsystem.service.RoundCriterionService;
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
@RequestMapping("/api/round_criteria")
@RequiredArgsConstructor
public class RoundCriterionController {

  private final RoundCriterionService roundCriterionService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<PageResponse<RoundCriterionResponse>>> getRoundCriteria(
      @RequestParam(name = "round_id", required = false) Integer roundId,
      @PageableDefault(size = 5, sort = "roundCriterionId") Pageable pageable) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Round criteria retrieved",
            roundCriterionService.getRoundCriteria(roundId, pageable)));
  }

  @GetMapping("/{roundCriterionId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<RoundCriterionResponse>> getRoundCriterion(
      @PathVariable Integer roundCriterionId) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Round criterion retrieved",
            roundCriterionService.getRoundCriterion(roundCriterionId)));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<RoundCriterionResponse>> createRoundCriterion(
      @Valid @RequestBody RoundCriterionRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.success(
                201,
                "Round criterion created",
                roundCriterionService.createRoundCriterion(request)));
  }

  @PutMapping("/{roundCriterionId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<RoundCriterionResponse>> updateRoundCriterion(
      @PathVariable Integer roundCriterionId, @Valid @RequestBody RoundCriterionRequest request) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Round criterion updated",
            roundCriterionService.updateRoundCriterion(roundCriterionId, request)));
  }

  @DeleteMapping("/{roundCriterionId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteRoundCriterion(@PathVariable Integer roundCriterionId) {
    roundCriterionService.deleteRoundCriterion(roundCriterionId);
    return ResponseEntity.noContent().build();
  }
}
