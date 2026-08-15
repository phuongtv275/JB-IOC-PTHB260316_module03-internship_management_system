package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.AssessmentRoundDetailResponse;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundResponse;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.PhaseReference;
import com.example.internshipmanagementsystem.entity.AssessmentRound;
import com.example.internshipmanagementsystem.repository.RoundCriterionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssessmentRoundMapper {

  private final RoundCriterionRepository roundCriterionRepository;
  private final RoundCriterionMapper roundCriterionMapper;

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

  public AssessmentRoundSummaryResponse toSummaryResponse(AssessmentRound round) {
    return new AssessmentRoundSummaryResponse(
        round.getRoundId(),
        round.getRoundName(),
        round.getStartDate(),
        round.getEndDate(),
        round.isActive(),
        roundCriterionRepository.countByRoundRoundId(round.getRoundId()),
        phase(round));
  }

  public AssessmentRoundDetailResponse toDetailResponse(AssessmentRound round) {
    return new AssessmentRoundDetailResponse(
        round.getRoundId(),
        round.getRoundName(),
        round.getStartDate(),
        round.getEndDate(),
        round.getDescription(),
        round.isActive(),
        phase(round),
        roundCriterionRepository.findAllByRoundRoundId(round.getRoundId()).stream()
            .map(roundCriterionMapper::toDetailResponse)
            .toList());
  }

  private PhaseReference phase(AssessmentRound round) {
    return new PhaseReference(
        round.getPhase().getPhaseId(),
        round.getPhase().getPhaseName(),
        round.getPhase().getStartDate(),
        round.getPhase().getEndDate());
  }
}
