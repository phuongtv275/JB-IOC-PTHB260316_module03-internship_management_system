package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

  boolean existsByStudentCode(String studentCode);

  boolean existsByStudentCodeAndStudentIdNot(String studentCode, Long studentId);
}
