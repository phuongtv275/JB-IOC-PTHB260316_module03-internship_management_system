package com.example.internshipmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "roundcriteria",
    uniqueConstraints = @UniqueConstraint(columnNames = {"roundid", "criterionid"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoundCriterion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "roundcriterionid")
  private Long roundCriterionId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "roundid")
  private AssessmentRound round;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "criterionid")
  private EvaluationCriterion criterion;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal weight;

  @Column(name = "createdat", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updatedat", nullable = false)
  private LocalDateTime updatedAt;

  private RoundCriterion(AssessmentRound round, EvaluationCriterion criterion, BigDecimal weight) {
    this.round = round;
    this.criterion = criterion;
    this.weight = weight;
  }

  public static RoundCriterion create(
      AssessmentRound round, EvaluationCriterion criterion, BigDecimal weight) {
    return new RoundCriterion(round, criterion, weight);
  }

  public void update(BigDecimal weight) {
    this.weight = weight;
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
