package com.example.internshipmanagementsystem.controller;

import com.example.internshipmanagementsystem.dto.request.StudentProfileRequest;
import com.example.internshipmanagementsystem.dto.response.ApiResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.StudentDetailResponse;
import com.example.internshipmanagementsystem.dto.response.StudentSummaryResponse;
import com.example.internshipmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

  private final StudentService studentService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR')")
  public ResponseEntity<ApiResponse<PageResponse<StudentSummaryResponse>>> getStudents(
      @AuthenticationPrincipal UserDetails userDetails,
      @PageableDefault(size = 5, sort = "studentId") Pageable pageable) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Students retrieved",
            studentService.getStudents(userDetails.getUsername(), pageable)));
  }

  @GetMapping("/{studentId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<StudentDetailResponse>> getStudent(
      @PathVariable Integer studentId, @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Student retrieved",
            studentService.getStudent(studentId, userDetails.getUsername())));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<StudentDetailResponse>> createStudent(
      @Valid @RequestBody StudentProfileRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(201, "Student created", studentService.createStudent(request)));
  }

  @PutMapping("/{studentId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
  public ResponseEntity<ApiResponse<StudentDetailResponse>> updateStudent(
      @PathVariable Integer studentId,
      @Valid @RequestBody StudentProfileRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Student updated",
            studentService.updateStudent(studentId, request, userDetails.getUsername())));
  }
}
