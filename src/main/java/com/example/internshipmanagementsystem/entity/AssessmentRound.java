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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assessmentrounds")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssessmentRound {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "roundid")
  private Long roundId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "phaseid")
  private InternshipPhase phase;

  @Column(name = "roundname", nullable = false, length = 100)
  private String roundName;

  @Column(name = "startdate", nullable = false)
  private LocalDate startDate;

  @Column(name = "enddate", nullable = false)
  private LocalDate endDate;

  private String description;

  @Column(name = "isactive", nullable = false)
  private boolean active;

  @Column(name = "createdat", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updatedat", nullable = false)
  private LocalDateTime updatedAt;

  private AssessmentRound(
      InternshipPhase phase,
      String roundName,
      LocalDate startDate,
      LocalDate endDate,
      String description) {
    this.phase = phase;
    this.roundName = roundName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
    this.active = true;
  }

  public static AssessmentRound create(
      InternshipPhase phase,
      String roundName,
      LocalDate startDate,
      LocalDate endDate,
      String description) {
    return new AssessmentRound(phase, roundName, startDate, endDate, description);
  }

  public void update(
      InternshipPhase phase,
      String roundName,
      LocalDate startDate,
      LocalDate endDate,
      String description) {
    this.phase = phase;
    this.roundName = roundName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
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
