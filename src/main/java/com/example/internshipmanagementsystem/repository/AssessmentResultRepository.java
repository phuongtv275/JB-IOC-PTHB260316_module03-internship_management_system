package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.AssessmentResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {

  boolean existsByRoundRoundId(Long roundId);

  boolean existsByAssignmentAssignmentIdAndRoundRoundIdAndCriterionCriterionId(
      Long assignmentId, Long roundId, Long criterionId);

  List<AssessmentResult> findByAssignmentMentorUserUsername(String username);

  List<AssessmentResult> findByAssignmentStudentUserUsername(String username);
}
