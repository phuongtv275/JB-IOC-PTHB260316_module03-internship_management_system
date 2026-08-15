package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.AssessmentRoundRequest;
import com.example.internshipmanagementsystem.dto.request.RoundCriterionInput;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundDetailResponse;
import com.example.internshipmanagementsystem.dto.response.AssessmentRoundSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.entity.AssessmentRound;
import com.example.internshipmanagementsystem.entity.EvaluationCriterion;
import com.example.internshipmanagementsystem.entity.InternshipPhase;
import com.example.internshipmanagementsystem.entity.RoundCriterion;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.InvalidAssessmentRoundException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.AssessmentRoundMapper;
import com.example.internshipmanagementsystem.repository.AssessmentResultRepository;
import com.example.internshipmanagementsystem.repository.AssessmentRoundRepository;
import com.example.internshipmanagementsystem.repository.EvaluationCriterionRepository;
import com.example.internshipmanagementsystem.repository.InternshipPhaseRepository;
import com.example.internshipmanagementsystem.repository.RoundCriterionRepository;
import com.example.internshipmanagementsystem.service.AssessmentRoundService;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssessmentRoundServiceImpl implements AssessmentRoundService {

  private final AssessmentRoundRepository assessmentRoundRepository;
  private final AssessmentResultRepository assessmentResultRepository;
  private final InternshipPhaseRepository internshipPhaseRepository;
  private final EvaluationCriterionRepository evaluationCriterionRepository;
  private final RoundCriterionRepository roundCriterionRepository;
  private final AssessmentRoundMapper assessmentRoundMapper;

  @Override
  public PageResponse<AssessmentRoundSummaryResponse> getRounds(
      Integer phaseId, Pageable pageable) {
    Page<AssessmentRound> rounds =
        phaseId == null
            ? assessmentRoundRepository.findAll(pageable)
            : assessmentRoundRepository.findByPhasePhaseId(phaseId, pageable);
    return PageResponse.from(rounds.map(assessmentRoundMapper::toSummaryResponse));
  }

  @Override
  public AssessmentRoundDetailResponse getRound(Integer roundId) {
    return assessmentRoundMapper.toDetailResponse(findRound(roundId));
  }

  @Override
  @Transactional
  public AssessmentRoundDetailResponse createRound(AssessmentRoundRequest request) {
    validateDates(request.startDate(), request.endDate());
    AssessmentRound round =
        assessmentRoundRepository.save(
            AssessmentRound.create(
                findPhase(request.phaseId()),
                request.roundName(),
                request.startDate(),
                request.endDate(),
                request.description()));
    createCriteria(round, request.criteria());
    return assessmentRoundMapper.toDetailResponse(round);
  }

  @Override
  @Transactional
  public AssessmentRoundDetailResponse updateRound(
      Integer roundId, AssessmentRoundRequest request) {
    validateDates(request.startDate(), request.endDate());
    AssessmentRound round = findRound(roundId);
    if (!Objects.equals(round.getPhase().getPhaseId(), request.phaseId())
        && assessmentResultRepository.existsByRoundRoundId(roundId)) {
      throw new InvalidAssessmentRoundException(
          "Assessment round phase cannot change after results exist");
    }
    round.update(
        findPhase(request.phaseId()),
        request.roundName(),
        request.startDate(),
        request.endDate(),
        request.description());
    return assessmentRoundMapper.toDetailResponse(round);
  }

  @Override
  @Transactional
  public void deleteRound(Integer roundId) {
    assessmentRoundRepository.delete(findRound(roundId));
  }

  private void createCriteria(AssessmentRound round, List<RoundCriterionInput> criteria) {
    if (criteria == null) {
      return;
    }
    Set<Integer> criterionIds = new HashSet<>();
    for (RoundCriterionInput input : criteria) {
      if (!criterionIds.add(input.criterionId())) {
        throw new DuplicateResourceException("Criterion is already in the assessment round");
      }
      EvaluationCriterion criterion = findCriterion(input.criterionId());
      roundCriterionRepository.save(RoundCriterion.create(round, criterion, input.weight()));
    }
  }

  private AssessmentRound findRound(Integer roundId) {
    return assessmentRoundRepository
        .findById(roundId)
        .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found"));
  }

  private InternshipPhase findPhase(Integer phaseId) {
    return internshipPhaseRepository
        .findById(phaseId)
        .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found"));
  }

  private EvaluationCriterion findCriterion(Integer criterionId) {
    return evaluationCriterionRepository
        .findById(criterionId)
        .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found"));
  }

  private void validateDates(LocalDate startDate, LocalDate endDate) {
    if (endDate.isBefore(startDate)) {
      throw new InvalidAssessmentRoundException("End date must be after or equal to start date");
    }
  }
}
