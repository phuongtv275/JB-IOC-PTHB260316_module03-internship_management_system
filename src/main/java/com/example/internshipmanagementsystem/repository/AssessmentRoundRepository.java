package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.AssessmentRound;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRoundRepository extends JpaRepository<AssessmentRound, Integer> {

  List<AssessmentRound> findByPhasePhaseId(Integer phaseId);
}
