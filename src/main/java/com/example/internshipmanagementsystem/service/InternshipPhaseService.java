package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.InternshipPhaseRequest;
import com.example.internshipmanagementsystem.dto.response.InternshipPhaseResponse;
import java.util.List;

public interface InternshipPhaseService {

  List<InternshipPhaseResponse> getPhases();

  InternshipPhaseResponse getPhase(Long phaseId);

  InternshipPhaseResponse createPhase(InternshipPhaseRequest request);

  InternshipPhaseResponse updatePhase(Long phaseId, InternshipPhaseRequest request);

  void deletePhase(Long phaseId);
}
