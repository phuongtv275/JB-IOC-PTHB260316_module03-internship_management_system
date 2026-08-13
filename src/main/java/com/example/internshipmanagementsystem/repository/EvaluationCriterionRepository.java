package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.EvaluationCriterion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationCriterionRepository extends JpaRepository<EvaluationCriterion, Integer> {

  boolean existsByCriterionName(String criterionName);

  boolean existsByCriterionNameAndCriterionIdNot(String criterionName, Integer criterionId);
}
