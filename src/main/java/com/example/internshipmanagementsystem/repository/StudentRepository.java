package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {

  @Override
  @EntityGraph(attributePaths = "user")
  Page<Student> findAll(Pageable pageable);

  boolean existsByStudentCode(String studentCode);

  boolean existsByStudentCodeAndStudentIdNot(String studentCode, Integer studentId);
}
