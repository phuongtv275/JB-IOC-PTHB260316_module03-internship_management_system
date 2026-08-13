package com.example.internshipmanagementsystem.controller;

import com.example.internshipmanagementsystem.dto.request.InternshipPhaseRequest;
import com.example.internshipmanagementsystem.dto.response.ApiResponse;
import com.example.internshipmanagementsystem.dto.response.InternshipPhaseResponse;
import com.example.internshipmanagementsystem.service.InternshipPhaseService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/internship_phases")
@RequiredArgsConstructor
public class InternshipPhaseController {

  private final InternshipPhaseService internshipPhaseService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<List<InternshipPhaseResponse>>> getPhases() {
    return ResponseEntity.ok(
        ApiResponse.success(
            200, "Internship phases retrieved", internshipPhaseService.getPhases()));
  }

  @GetMapping("/{phaseId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<InternshipPhaseResponse>> getPhase(@PathVariable Long phaseId) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200, "Internship phase retrieved", internshipPhaseService.getPhase(phaseId)));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<InternshipPhaseResponse>> createPhase(
      @Valid @RequestBody InternshipPhaseRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.success(
                201, "Internship phase created", internshipPhaseService.createPhase(request)));
  }

  @PutMapping("/{phaseId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<InternshipPhaseResponse>> updatePhase(
      @PathVariable Long phaseId, @Valid @RequestBody InternshipPhaseRequest request) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200, "Internship phase updated", internshipPhaseService.updatePhase(phaseId, request)));
  }

  @DeleteMapping("/{phaseId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deletePhase(@PathVariable Long phaseId) {
    internshipPhaseService.deletePhase(phaseId);
    return ResponseEntity.noContent().build();
  }
}
