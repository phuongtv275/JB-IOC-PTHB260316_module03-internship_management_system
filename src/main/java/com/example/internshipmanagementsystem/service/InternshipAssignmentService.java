package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.InternshipAssignmentRequest;
import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.entity.AssignmentStatus;
import org.springframework.data.domain.Pageable;

public interface InternshipAssignmentService {

  PageResponse<InternshipAssignmentResponse> getAssignments(
      String actorUsername, Pageable pageable);

  InternshipAssignmentResponse getAssignment(Integer assignmentId, String actorUsername);

  InternshipAssignmentResponse createAssignment(InternshipAssignmentRequest request);

  InternshipAssignmentResponse updateStatus(Integer assignmentId, AssignmentStatus status);
}
