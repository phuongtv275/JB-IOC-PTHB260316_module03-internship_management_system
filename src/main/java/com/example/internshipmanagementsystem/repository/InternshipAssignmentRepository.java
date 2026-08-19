package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipAssignmentRepository
    extends JpaRepository<InternshipAssignment, Integer> {

  boolean existsByStudentStudentIdAndPhasePhaseId(Integer studentId, Integer phaseId);

  Page<InternshipAssignment> findByMentorUserUsername(String username, Pageable pageable);

  Page<InternshipAssignment> findByStudentUserUsername(String username, Pageable pageable);
}
