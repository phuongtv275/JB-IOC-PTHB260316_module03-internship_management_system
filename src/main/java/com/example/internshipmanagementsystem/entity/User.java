package com.example.internshipmanagementsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "userid")
  private Integer userId;

  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @Column(name = "passwordhash", nullable = false)
  private String passwordHash;

  @Column(name = "fullname", nullable = false, length = 100)
  private String fullName;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(name = "phonenumber", length = 20)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Column(name = "isactive", nullable = false)
  private boolean active;

  @Column(name = "createdat", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updatedat", nullable = false)
  private LocalDateTime updatedAt;

  private User(
      String username,
      String passwordHash,
      String fullName,
      String email,
      String phoneNumber,
      Role role) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.fullName = fullName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.role = role;
    this.active = true;
  }

  public static User create(
      String username,
      String passwordHash,
      String fullName,
      String email,
      String phoneNumber,
      Role role) {
    return new User(username, passwordHash, fullName, email, phoneNumber, role);
  }

  public void update(
      String username, String passwordHash, String fullName, String email, String phoneNumber) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.fullName = fullName;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public void changeActiveStatus(boolean active) {
    this.active = active;
  }

  public void changeRole(Role role) {
    this.role = role;
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
