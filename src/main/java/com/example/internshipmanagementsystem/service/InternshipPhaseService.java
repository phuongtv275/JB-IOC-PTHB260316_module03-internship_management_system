package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.InternshipPhaseRequest;
import com.example.internshipmanagementsystem.dto.response.InternshipPhaseResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface InternshipPhaseService {

  PageResponse<InternshipPhaseResponse> getPhases(Pageable pageable);

  InternshipPhaseResponse getPhase(Integer phaseId);

  InternshipPhaseResponse createPhase(InternshipPhaseRequest request);

  InternshipPhaseResponse updatePhase(Integer phaseId, InternshipPhaseRequest request);

  void deletePhase(Integer phaseId);
}
