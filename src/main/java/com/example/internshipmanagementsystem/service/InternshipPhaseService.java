package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.InternshipPhaseRequest;
import com.example.internshipmanagementsystem.dto.response.InternshipPhaseResponse;
import java.util.List;

public interface InternshipPhaseService {

  List<InternshipPhaseResponse> getPhases();

  InternshipPhaseResponse getPhase(Integer phaseId);

  InternshipPhaseResponse createPhase(InternshipPhaseRequest request);

  InternshipPhaseResponse updatePhase(Integer phaseId, InternshipPhaseRequest request);

  void deletePhase(Integer phaseId);
}
