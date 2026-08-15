package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentDetailResponse;
import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.MentorReference;
import com.example.internshipmanagementsystem.dto.response.PhaseReference;
import com.example.internshipmanagementsystem.dto.response.StudentReference;
import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import org.springframework.stereotype.Component;

@Component
public class InternshipAssignmentMapper {

  public InternshipAssignmentSummaryResponse toSummaryResponse(InternshipAssignment assignment) {
    return new InternshipAssignmentSummaryResponse(
        assignment.getAssignmentId(),
        assignment.getStatus(),
        assignment.getAssignedDate(),
        new StudentReference(
            assignment.getStudent().getStudentId(),
            assignment.getStudent().getStudentCode(),
            assignment.getStudent().getUser().getFullName(),
            assignment.getStudent().getClassName()),
        new MentorReference(
            assignment.getMentor().getMentorId(),
            assignment.getMentor().getUser().getFullName(),
            assignment.getMentor().getDepartment()),
        new PhaseReference(
            assignment.getPhase().getPhaseId(),
            assignment.getPhase().getPhaseName(),
            assignment.getPhase().getStartDate(),
            assignment.getPhase().getEndDate()));
  }

  public InternshipAssignmentDetailResponse toDetailResponse(InternshipAssignment assignment) {
    InternshipAssignmentSummaryResponse summary = toSummaryResponse(assignment);
    return new InternshipAssignmentDetailResponse(
        summary.assignmentId(),
        summary.status(),
        summary.assignedAt(),
        summary.student(),
        summary.mentor(),
        summary.phase());
  }
}
