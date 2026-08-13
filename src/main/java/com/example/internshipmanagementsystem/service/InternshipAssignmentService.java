package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.InternshipAssignmentRequest;
import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentResponse;
import com.example.internshipmanagementsystem.entity.AssignmentStatus;
import java.util.List;

public interface InternshipAssignmentService {

  List<InternshipAssignmentResponse> getAssignments(String actorUsername);

  InternshipAssignmentResponse getAssignment(Integer assignmentId, String actorUsername);

  InternshipAssignmentResponse createAssignment(InternshipAssignmentRequest request);

  InternshipAssignmentResponse updateStatus(Integer assignmentId, AssignmentStatus status);
}
