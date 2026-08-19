package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.MentorProfileRequest;
import com.example.internshipmanagementsystem.dto.response.MentorDetailResponse;
import com.example.internshipmanagementsystem.dto.response.MentorSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface MentorService {

  PageResponse<MentorSummaryResponse> getMentors(Pageable pageable);

  MentorDetailResponse getMentor(Integer mentorId, String actorUsername);

  MentorDetailResponse createMentor(MentorProfileRequest request);

  MentorDetailResponse updateMentor(
      Integer mentorId, MentorProfileRequest request, String actorUsername);
}
