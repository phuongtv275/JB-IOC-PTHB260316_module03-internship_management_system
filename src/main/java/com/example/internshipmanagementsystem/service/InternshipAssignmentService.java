package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.InternshipAssignmentRequest;
import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentDetailResponse;
import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.entity.AssignmentStatus;
import org.springframework.data.domain.Pageable;

public interface InternshipAssignmentService {

  PageResponse<InternshipAssignmentSummaryResponse> getAssignments(
      String actorUsername, Pageable pageable);

  InternshipAssignmentDetailResponse getAssignment(Integer assignmentId, String actorUsername);

  InternshipAssignmentDetailResponse createAssignment(InternshipAssignmentRequest request);

  InternshipAssignmentDetailResponse updateStatus(Integer assignmentId, AssignmentStatus status);
}
