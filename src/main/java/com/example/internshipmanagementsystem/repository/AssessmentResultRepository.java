package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.AssessmentResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Integer> {

  boolean existsByRoundRoundId(Integer roundId);

  boolean existsByAssignmentAssignmentIdAndRoundRoundIdAndCriterionCriterionId(
      Integer assignmentId, Integer roundId, Integer criterionId);

  Page<AssessmentResult> findByAssignmentMentorUserUsername(String username, Pageable pageable);

  Page<AssessmentResult> findByAssignmentStudentUserUsername(String username, Pageable pageable);
}
