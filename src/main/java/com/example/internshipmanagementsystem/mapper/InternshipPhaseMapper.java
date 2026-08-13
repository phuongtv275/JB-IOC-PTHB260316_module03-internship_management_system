package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.InternshipPhaseResponse;
import com.example.internshipmanagementsystem.entity.InternshipPhase;
import org.springframework.stereotype.Component;

@Component
public class InternshipPhaseMapper {

  public InternshipPhaseResponse toResponse(InternshipPhase phase) {
    return new InternshipPhaseResponse(
        phase.getPhaseId(),
        phase.getPhaseName(),
        phase.getStartDate(),
        phase.getEndDate(),
        phase.getDescription());
  }
}
