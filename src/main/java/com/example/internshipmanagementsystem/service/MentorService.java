package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.MentorProfileRequest;
import com.example.internshipmanagementsystem.dto.response.MentorResponse;
import java.util.List;

public interface MentorService {

  List<MentorResponse> getMentors();

  MentorResponse getMentor(Integer mentorId, String actorUsername);

  MentorResponse createMentor(MentorProfileRequest request);

  MentorResponse updateMentor(Integer mentorId, MentorProfileRequest request, String actorUsername);
}
