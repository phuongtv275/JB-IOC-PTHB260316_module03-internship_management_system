package com.example.internshipmanagementsystem.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.dto.request.AssessmentRoundRequest;
import com.example.internshipmanagementsystem.entity.AssessmentRound;
import com.example.internshipmanagementsystem.entity.InternshipPhase;
import com.example.internshipmanagementsystem.exception.InvalidAssessmentRoundException;
import com.example.internshipmanagementsystem.mapper.AssessmentRoundMapper;
import com.example.internshipmanagementsystem.repository.AssessmentResultRepository;
import com.example.internshipmanagementsystem.repository.AssessmentRoundRepository;
import com.example.internshipmanagementsystem.repository.EvaluationCriterionRepository;
import com.example.internshipmanagementsystem.repository.InternshipPhaseRepository;
import com.example.internshipmanagementsystem.repository.RoundCriterionRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AssessmentRoundServiceImplTest {

  @Test
  void shouldRejectPhaseChange_whenRoundAlreadyHasResults() {
    AssessmentRoundRepository roundRepository = Mockito.mock(AssessmentRoundRepository.class);
    AssessmentResultRepository resultRepository = Mockito.mock(AssessmentResultRepository.class);
    InternshipPhaseRepository phaseRepository = Mockito.mock(InternshipPhaseRepository.class);
    AssessmentRound round = Mockito.mock(AssessmentRound.class);
    InternshipPhase currentPhase = Mockito.mock(InternshipPhase.class);
    InternshipPhase otherPhase = Mockito.mock(InternshipPhase.class);
    when(round.getPhase()).thenReturn(currentPhase);
    when(currentPhase.getPhaseId()).thenReturn(1);
    when(otherPhase.getPhaseId()).thenReturn(2);
    when(roundRepository.findById(1)).thenReturn(Optional.of(round));
    when(phaseRepository.findById(2)).thenReturn(Optional.of(otherPhase));
    when(resultRepository.existsByRoundRoundId(1)).thenReturn(true);
    AssessmentRoundServiceImpl service =
        new AssessmentRoundServiceImpl(
            roundRepository,
            resultRepository,
            phaseRepository,
            Mockito.mock(EvaluationCriterionRepository.class),
            Mockito.mock(RoundCriterionRepository.class),
            new AssessmentRoundMapper());

    assertThatThrownBy(
            () ->
                service.updateRound(
                    1,
                    new AssessmentRoundRequest(
                        2,
                        "Round",
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 1, 2),
                        null,
                        null)))
        .isInstanceOf(InvalidAssessmentRoundException.class);
  }
}
