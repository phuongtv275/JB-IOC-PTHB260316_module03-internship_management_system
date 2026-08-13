package com.example.internshipmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "internshipphases")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InternshipPhase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "phaseid")
  private Long phaseId;

  @Column(name = "phasename", nullable = false, unique = true, length = 100)
  private String phaseName;

  @Column(name = "startdate", nullable = false)
  private LocalDate startDate;

  @Column(name = "enddate", nullable = false)
  private LocalDate endDate;

  private String description;

  @Column(name = "createdat", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updatedat", nullable = false)
  private LocalDateTime updatedAt;

  private InternshipPhase(
      String phaseName, LocalDate startDate, LocalDate endDate, String description) {
    this.phaseName = phaseName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
  }

  public static InternshipPhase create(
      String phaseName, LocalDate startDate, LocalDate endDate, String description) {
    return new InternshipPhase(phaseName, startDate, endDate, description);
  }

  public void update(String phaseName, LocalDate startDate, LocalDate endDate, String description) {
    this.phaseName = phaseName;
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
