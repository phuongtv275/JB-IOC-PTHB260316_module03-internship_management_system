package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.StudentProfileRequest;
import com.example.internshipmanagementsystem.dto.response.StudentResponse;
import java.util.List;

public interface StudentService {

  List<StudentResponse> getStudents(String actorUsername);

  StudentResponse getStudent(Integer studentId, String actorUsername);

  StudentResponse createStudent(StudentProfileRequest request);

  StudentResponse updateStudent(
      Integer studentId, StudentProfileRequest request, String actorUsername);
}
