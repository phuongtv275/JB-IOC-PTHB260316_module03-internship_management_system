package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.AssessmentRound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRoundRepository extends JpaRepository<AssessmentRound, Integer> {

  Page<AssessmentRound> findByPhasePhaseId(Integer phaseId, Pageable pageable);
}
