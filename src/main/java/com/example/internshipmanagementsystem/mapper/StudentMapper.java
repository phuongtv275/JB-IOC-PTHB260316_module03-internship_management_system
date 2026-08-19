package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.StudentDetailResponse;
import com.example.internshipmanagementsystem.dto.response.StudentResponse;
import com.example.internshipmanagementsystem.dto.response.StudentSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

  private final UserMapper userMapper;

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

  public StudentSummaryResponse toSummaryResponse(Student student) {
    return new StudentSummaryResponse(
        student.getStudentId(),
        student.getStudentCode(),
        student.getUser().getFullName(),
        student.getClassName(),
        student.getMajor(),
        student.getUser().isActive());
  }

  public StudentDetailResponse toDetailResponse(Student student) {
    UserResponse account = userMapper.toResponse(student.getUser());
    return new StudentDetailResponse(
        student.getStudentId(),
        student.getStudentCode(),
        student.getMajor(),
        student.getClassName(),
        student.getDateOfBirth(),
        student.getAddress(),
        account,
        student.getCreatedAt(),
        student.getUpdatedAt());
  }
}
