package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.AssessmentRoundResponse;
import com.example.internshipmanagementsystem.entity.AssessmentRound;
import org.springframework.stereotype.Component;

@Component
public class AssessmentRoundMapper {

  public AssessmentRoundResponse toResponse(AssessmentRound round) {
    return new AssessmentRoundResponse(
        round.getRoundId(),
        round.getPhase().getPhaseId(),
        round.getRoundName(),
        round.getStartDate(),
        round.getEndDate(),
        round.getDescription(),
        round.isActive());
  }
}
