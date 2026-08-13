package com.example.internshipmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "internshipassignments",
    uniqueConstraints = @UniqueConstraint(columnNames = {"studentid", "phaseid"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InternshipAssignment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "assignmentid")
  private Long assignmentId;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "studentid")
  private Student student;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "mentorid")
  private Mentor mentor;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "phaseid")
  private InternshipPhase phase;

  @Column(name = "assigneddate", nullable = false)
  private LocalDateTime assignedDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AssignmentStatus status;

  @Column(name = "createdat", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updatedat", nullable = false)
  private LocalDateTime updatedAt;

  private InternshipAssignment(Student student, Mentor mentor, InternshipPhase phase) {
    this.student = student;
    this.mentor = mentor;
    this.phase = phase;
    this.assignedDate = LocalDateTime.now();
    this.status = AssignmentStatus.PENDING;
  }

  public static InternshipAssignment create(Student student, Mentor mentor, InternshipPhase phase) {
    return new InternshipAssignment(student, mentor, phase);
  }

  public void changeStatus(AssignmentStatus status) {
    this.status = status;
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
