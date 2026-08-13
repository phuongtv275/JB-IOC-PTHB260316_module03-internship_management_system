package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.AssessmentResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Integer> {

  boolean existsByRoundRoundId(Integer roundId);

  boolean existsByAssignmentAssignmentIdAndRoundRoundIdAndCriterionCriterionId(
      Integer assignmentId, Integer roundId, Integer criterionId);

  List<AssessmentResult> findByAssignmentMentorUserUsername(String username);

  List<AssessmentResult> findByAssignmentStudentUserUsername(String username);
}
