package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.AssessmentRound;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRoundRepository extends JpaRepository<AssessmentRound, Long> {

  List<AssessmentRound> findByPhasePhaseId(Long phaseId);
}
