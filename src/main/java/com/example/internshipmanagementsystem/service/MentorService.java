package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.MentorProfileRequest;
import com.example.internshipmanagementsystem.dto.response.MentorResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface MentorService {

  PageResponse<MentorResponse> getMentors(Pageable pageable);

  MentorResponse getMentor(Integer mentorId, String actorUsername);

  MentorResponse createMentor(MentorProfileRequest request);

  MentorResponse updateMentor(Integer mentorId, MentorProfileRequest request, String actorUsername);
}
