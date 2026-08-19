package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.StudentProfileRequest;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.StudentResponse;
import org.springframework.data.domain.Pageable;

public interface StudentService {

  PageResponse<StudentResponse> getStudents(String actorUsername, Pageable pageable);

  StudentResponse getStudent(Integer studentId, String actorUsername);

  StudentResponse createStudent(StudentProfileRequest request);

  StudentResponse updateStudent(
      Integer studentId, StudentProfileRequest request, String actorUsername);
}
