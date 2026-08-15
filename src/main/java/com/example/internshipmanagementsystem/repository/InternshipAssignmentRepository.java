package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipAssignmentRepository
    extends JpaRepository<InternshipAssignment, Integer> {

  @Override
  @EntityGraph(attributePaths = {"student", "student.user", "mentor", "mentor.user", "phase"})
  Page<InternshipAssignment> findAll(Pageable pageable);

  boolean existsByStudentStudentIdAndPhasePhaseId(Integer studentId, Integer phaseId);

  @EntityGraph(attributePaths = {"student", "student.user", "mentor", "mentor.user", "phase"})
  Page<InternshipAssignment> findByMentorUserUsername(String username, Pageable pageable);

  @EntityGraph(attributePaths = {"student", "student.user", "mentor", "mentor.user", "phase"})
  Page<InternshipAssignment> findByStudentUserUsername(String username, Pageable pageable);
}
