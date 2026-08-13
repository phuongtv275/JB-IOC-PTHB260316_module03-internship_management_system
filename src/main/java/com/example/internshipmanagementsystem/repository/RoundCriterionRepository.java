package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.RoundCriterion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundCriterionRepository extends JpaRepository<RoundCriterion, Long> {

  List<RoundCriterion> findByRoundRoundId(Long roundId);

  boolean existsByRoundRoundIdAndCriterionCriterionId(Long roundId, Long criterionId);
}
