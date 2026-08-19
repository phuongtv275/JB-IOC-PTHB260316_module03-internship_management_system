package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.AssessmentResultResponse;
import com.example.internshipmanagementsystem.dto.response.CriterionReference;
import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.MentorReference;
import com.example.internshipmanagementsystem.dto.response.PhaseReference;
import com.example.internshipmanagementsystem.dto.response.RoundReference;
import com.example.internshipmanagementsystem.dto.response.StudentReference;
import com.example.internshipmanagementsystem.dto.response.UserSummaryResponse;
import com.example.internshipmanagementsystem.entity.AssessmentResult;
import org.springframework.stereotype.Component;

@Component
public class AssessmentResultMapper {

  public AssessmentResultResponse toResponse(AssessmentResult result) {
    return new AssessmentResultResponse(
        result.getResultId(),
        assignment(result),
        new RoundReference(result.getRound().getRoundId(), result.getRound().getRoundName()),
        new CriterionReference(
            result.getCriterion().getCriterionId(),
            result.getCriterion().getCriterionName(),
            result.getCriterion().getDescription(),
            result.getCriterion().getMaxScore()),
        result.getScore(),
        result.getComments(),
        new UserSummaryResponse(
            result.getEvaluatedBy().getUserId(),
            result.getEvaluatedBy().getUsername(),
            result.getEvaluatedBy().getFullName(),
            result.getEvaluatedBy().getRole(),
            result.getEvaluatedBy().isActive()),
        result.getEvaluationDate());
  }

  private InternshipAssignmentSummaryResponse assignment(AssessmentResult result) {
    var assignment = result.getAssignment();
    return new InternshipAssignmentSummaryResponse(
        assignment.getAssignmentId(),
        assignment.getStatus(),
        assignment.getAssignedDate(),
        new StudentReference(
            assignment.getStudent().getStudentId(), assignment.getStudent().getStudentCode(),
            assignment.getStudent().getUser().getFullName(),
                assignment.getStudent().getClassName()),
        new MentorReference(
            assignment.getMentor().getMentorId(),
            assignment.getMentor().getUser().getFullName(),
            assignment.getMentor().getDepartment()),
        new PhaseReference(
            assignment.getPhase().getPhaseId(), assignment.getPhase().getPhaseName(),
            assignment.getPhase().getStartDate(), assignment.getPhase().getEndDate()));
  }
}
