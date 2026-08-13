package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.InternshipPhase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipPhaseRepository extends JpaRepository<InternshipPhase, Integer> {

  boolean existsByPhaseName(String phaseName);

  boolean existsByPhaseNameAndPhaseIdNot(String phaseName, Integer phaseId);
}
