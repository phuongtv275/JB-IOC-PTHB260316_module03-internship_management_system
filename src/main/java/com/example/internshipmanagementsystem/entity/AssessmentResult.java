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
    name = "assessmentresults",
    uniqueConstraints = @UniqueConstraint(columnNames = {"assignmentid", "roundid", "criterionid"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssessmentResult {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "resultid")
  private Long resultId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "assignmentid")
  private InternshipAssignment assignment;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "roundid")
  private AssessmentRound round;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "criterionid")
  private EvaluationCriterion criterion;

  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal score;

  private String comments;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "evaluatedby")
  private User evaluatedBy;

  @Column(name = "evaluationdate", nullable = false)
  private LocalDateTime evaluationDate;

  @Column(name = "createdat", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updatedat", nullable = false)
  private LocalDateTime updatedAt;

  private AssessmentResult(
      InternshipAssignment assignment,
      AssessmentRound round,
      EvaluationCriterion criterion,
      BigDecimal score,
      String comments,
      User evaluatedBy) {
    this.assignment = assignment;
    this.round = round;
    this.criterion = criterion;
    this.score = score;
    this.comments = comments;
    this.evaluatedBy = evaluatedBy;
    this.evaluationDate = LocalDateTime.now();
  }

  public static AssessmentResult create(
      InternshipAssignment assignment,
      AssessmentRound round,
      EvaluationCriterion criterion,
      BigDecimal score,
      String comments,
      User evaluatedBy) {
    return new AssessmentResult(assignment, round, criterion, score, comments, evaluatedBy);
  }

  public void update(BigDecimal score, String comments) {
    this.score = score;
    this.comments = comments;
    this.evaluationDate = LocalDateTime.now();
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
