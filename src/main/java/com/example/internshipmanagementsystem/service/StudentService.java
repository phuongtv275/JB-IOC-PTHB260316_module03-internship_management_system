package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.StudentProfileRequest;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.StudentDetailResponse;
import com.example.internshipmanagementsystem.dto.response.StudentSummaryResponse;
import org.springframework.data.domain.Pageable;

public interface StudentService {

  PageResponse<StudentSummaryResponse> getStudents(String actorUsername, Pageable pageable);

  StudentDetailResponse getStudent(Integer studentId, String actorUsername);

  StudentDetailResponse createStudent(StudentProfileRequest request);

  StudentDetailResponse updateStudent(
      Integer studentId, StudentProfileRequest request, String actorUsername);
}
