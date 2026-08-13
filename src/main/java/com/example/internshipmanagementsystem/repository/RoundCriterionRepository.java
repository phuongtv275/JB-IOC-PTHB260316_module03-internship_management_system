package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.RoundCriterion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundCriterionRepository extends JpaRepository<RoundCriterion, Integer> {

  List<RoundCriterion> findByRoundRoundId(Integer roundId);

  boolean existsByRoundRoundIdAndCriterionCriterionId(Integer roundId, Integer criterionId);
}
