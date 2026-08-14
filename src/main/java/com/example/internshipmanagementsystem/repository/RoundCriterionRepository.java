package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.RoundCriterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundCriterionRepository extends JpaRepository<RoundCriterion, Integer> {

  Page<RoundCriterion> findByRoundRoundId(Integer roundId, Pageable pageable);

  boolean existsByRoundRoundIdAndCriterionCriterionId(Integer roundId, Integer criterionId);
}
