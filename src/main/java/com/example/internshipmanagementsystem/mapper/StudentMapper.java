package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.StudentResponse;
import com.example.internshipmanagementsystem.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

  public StudentResponse toResponse(Student student) {
    return new StudentResponse(
        student.getStudentId(),
        student.getStudentCode(),
        student.getMajor(),
        student.getClassName(),
        student.getDateOfBirth(),
        student.getAddress(),
        student.getCreatedAt(),
        student.getUpdatedAt());
  }
}
