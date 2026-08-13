package com.example.internshipmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evaluationcriteria")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvaluationCriterion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "criterionid")
  private Integer criterionId;

  @Column(name = "criterionname", nullable = false, unique = true, length = 200)
  private String criterionName;

  private String description;

  @Column(name = "maxscore", nullable = false, precision = 5, scale = 2)
  private BigDecimal maxScore;

  @Column(name = "createdat", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updatedat", nullable = false)
  private LocalDateTime updatedAt;

  private EvaluationCriterion(String criterionName, String description, BigDecimal maxScore) {
    this.criterionName = criterionName;
    this.description = description;
    this.maxScore = maxScore;
  }

  public static EvaluationCriterion create(
      String criterionName, String description, BigDecimal maxScore) {
    return new EvaluationCriterion(criterionName, description, maxScore);
  }

  public void update(String criterionName, String description, BigDecimal maxScore) {
    this.criterionName = criterionName;
    this.description = description;
    this.maxScore = maxScore;
  }

  @PrePersist
  void initializeTimestamps() {
    createdAt = LocalDateTime.now();
    updatedAt = createdAt;
  }

  @PreUpdate
  void updateTimestamp() {
    updatedAt = LocalDateTime.now();
  }
}
