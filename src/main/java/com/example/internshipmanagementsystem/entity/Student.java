package com.example.internshipmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

  @Id private Integer studentId;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId
  @JoinColumn(name = "studentid")
  private User user;

  @Column(name = "studentcode", nullable = false, unique = true, length = 20)
  private String studentCode;

  @Column(length = 100)
  private String major;

  @Column(name = "class", length = 50)
  private String className;

  @Column(name = "dateofbirth")
  private LocalDate dateOfBirth;

  @Column(length = 255)
  private String address;

  @Column(name = "createdat", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updatedat", nullable = false)
  private LocalDateTime updatedAt;

  private Student(
      User user,
      String studentCode,
      String major,
      String className,
      LocalDate dateOfBirth,
      String address) {
    this.user = user;
    this.studentCode = studentCode;
    this.major = major;
    this.className = className;
    this.dateOfBirth = dateOfBirth;
    this.address = address;
  }

  public static Student create(
      User user,
      String studentCode,
      String major,
      String className,
      LocalDate dateOfBirth,
      String address) {
    return new Student(user, studentCode, major, className, dateOfBirth, address);
  }

  public void update(
      String studentCode, String major, String className, LocalDate dateOfBirth, String address) {
    this.studentCode = studentCode;
    this.major = major;
    this.className = className;
    this.dateOfBirth = dateOfBirth;
    this.address = address;
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
