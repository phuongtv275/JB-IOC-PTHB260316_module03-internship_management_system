package com.example.internshipmanagementsystem.controller;

import com.example.internshipmanagementsystem.dto.request.MentorProfileRequest;
import com.example.internshipmanagementsystem.dto.response.ApiResponse;
import com.example.internshipmanagementsystem.dto.response.MentorDetailResponse;
import com.example.internshipmanagementsystem.dto.response.MentorSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.service.MentorService;
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
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

  private final MentorService mentorService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
  public ResponseEntity<ApiResponse<PageResponse<MentorSummaryResponse>>> getMentors(
      @PageableDefault(size = 5, sort = "mentorId") Pageable pageable) {
    return ResponseEntity.ok(
        ApiResponse.success(200, "Mentors retrieved", mentorService.getMentors(pageable)));
  }

  @GetMapping("/{mentorId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR', 'STUDENT')")
  public ResponseEntity<ApiResponse<MentorDetailResponse>> getMentor(
      @PathVariable Integer mentorId, @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200, "Mentor retrieved", mentorService.getMentor(mentorId, userDetails.getUsername())));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<MentorDetailResponse>> createMentor(
      @Valid @RequestBody MentorProfileRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(201, "Mentor created", mentorService.createMentor(request)));
  }

  @PutMapping("/{mentorId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MENTOR')")
  public ResponseEntity<ApiResponse<MentorDetailResponse>> updateMentor(
      @PathVariable Integer mentorId,
      @Valid @RequestBody MentorProfileRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "Mentor updated",
            mentorService.updateMentor(mentorId, request, userDetails.getUsername())));
  }
}
