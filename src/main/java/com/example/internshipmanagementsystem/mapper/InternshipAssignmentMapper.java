package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentResponse;
import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import org.springframework.stereotype.Component;

@Component
public class InternshipAssignmentMapper {

  public InternshipAssignmentResponse toResponse(InternshipAssignment assignment) {
    return new InternshipAssignmentResponse(
        assignment.getAssignmentId(),
        assignment.getStudent().getStudentId(),
        assignment.getMentor().getMentorId(),
        assignment.getPhase().getPhaseId(),
        assignment.getAssignedDate(),
        assignment.getStatus());
  }
}
