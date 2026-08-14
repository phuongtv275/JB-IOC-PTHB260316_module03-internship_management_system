package com.example.internshipmanagementsystem.controller;

import com.example.internshipmanagementsystem.dto.request.AssignmentStatusRequest;
import com.example.internshipmanagementsystem.dto.request.InternshipAssignmentRequest;
import com.example.internshipmanagementsystem.dto.response.ApiResponse;
import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentResponse;
import com.example.internshipmanagementsystem.service.InternshipAssignmentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/internship_assignments")
@RequiredArgsConstructor
public class InternshipAssignmentController {

  private final InternshipAssignmentService internshipAssignmentService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<List<InternshipAssignmentResponse>>> getAssignments(
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Internship assignments retrieved",
            internshipAssignmentService.getAssignments(userDetails.getUsername())));
  }

  @GetMapping("/{assignmentId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<InternshipAssignmentResponse>> getAssignment(
      @PathVariable Integer assignmentId, @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Internship assignment retrieved",
            internshipAssignmentService.getAssignment(assignmentId, userDetails.getUsername())));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<InternshipAssignmentResponse>> createAssignment(
      @Valid @RequestBody InternshipAssignmentRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.success(
                201,
                "Internship assignment created",
                internshipAssignmentService.createAssignment(request)));
  }

  @PutMapping("/{assignmentId}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<InternshipAssignmentResponse>> updateStatus(
      @PathVariable Integer assignmentId, @Valid @RequestBody AssignmentStatusRequest request) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Internship assignment status updated",
            internshipAssignmentService.updateStatus(assignmentId, request.status())));
  }
}
