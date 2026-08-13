package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipAssignmentRepository extends JpaRepository<InternshipAssignment, Long> {

  boolean existsByStudentStudentIdAndPhasePhaseId(Long studentId, Long phaseId);

  List<InternshipAssignment> findByMentorUserUsername(String username);

  List<InternshipAssignment> findByStudentUserUsername(String username);
}
