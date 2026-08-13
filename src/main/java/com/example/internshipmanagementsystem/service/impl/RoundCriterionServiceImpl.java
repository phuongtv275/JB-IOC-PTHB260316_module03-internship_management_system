package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.RoundCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.RoundCriterionResponse;
import com.example.internshipmanagementsystem.entity.AssessmentRound;
import com.example.internshipmanagementsystem.entity.EvaluationCriterion;
import com.example.internshipmanagementsystem.entity.RoundCriterion;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.InvalidAssessmentRoundException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.RoundCriterionMapper;
import com.example.internshipmanagementsystem.repository.AssessmentRoundRepository;
import com.example.internshipmanagementsystem.repository.EvaluationCriterionRepository;
import com.example.internshipmanagementsystem.repository.RoundCriterionRepository;
import com.example.internshipmanagementsystem.service.RoundCriterionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoundCriterionServiceImpl implements RoundCriterionService {

  private final RoundCriterionRepository roundCriterionRepository;
  private final AssessmentRoundRepository assessmentRoundRepository;
  private final EvaluationCriterionRepository evaluationCriterionRepository;
  private final RoundCriterionMapper roundCriterionMapper;

  @Override
  public List<RoundCriterionResponse> getRoundCriteria(Integer roundId) {
    List<RoundCriterion> criteria =
        roundId == null
            ? roundCriterionRepository.findAll()
            : roundCriterionRepository.findByRoundRoundId(roundId);
    return criteria.stream().map(roundCriterionMapper::toResponse).toList();
  }

  @Override
  public RoundCriterionResponse getRoundCriterion(Integer roundCriterionId) {
    return roundCriterionMapper.toResponse(findRoundCriterion(roundCriterionId));
  }

  @Override
  @Transactional
  public RoundCriterionResponse createRoundCriterion(RoundCriterionRequest request) {
    if (roundCriterionRepository.existsByRoundRoundIdAndCriterionCriterionId(
        request.roundId(), request.criterionId())) {
      throw new DuplicateResourceException("Criterion is already in the assessment round");
    }
    RoundCriterion roundCriterion =
        RoundCriterion.create(
            findRound(request.roundId()), findCriterion(request.criterionId()), request.weight());
    return roundCriterionMapper.toResponse(roundCriterionRepository.save(roundCriterion));
  }

  @Override
  @Transactional
  public RoundCriterionResponse updateRoundCriterion(
      Integer roundCriterionId, RoundCriterionRequest request) {
    RoundCriterion roundCriterion = findRoundCriterion(roundCriterionId);
    if (!roundCriterion.getRound().getRoundId().equals(request.roundId())
        || !roundCriterion.getCriterion().getCriterionId().equals(request.criterionId())) {
      throw new InvalidAssessmentRoundException("Round criterion relationship cannot be changed");
    }
    roundCriterion.update(request.weight());
    return roundCriterionMapper.toResponse(roundCriterion);
  }

  @Override
  @Transactional
  public void deleteRoundCriterion(Integer roundCriterionId) {
    roundCriterionRepository.delete(findRoundCriterion(roundCriterionId));
  }

  private RoundCriterion findRoundCriterion(Integer roundCriterionId) {
    return roundCriterionRepository
        .findById(roundCriterionId)
        .orElseThrow(() -> new ResourceNotFoundException("Round criterion not found"));
  }

  private AssessmentRound findRound(Integer roundId) {
    return assessmentRoundRepository
        .findById(roundId)
        .orElseThrow(() -> new ResourceNotFoundException("Assessment round not found"));
  }

  private EvaluationCriterion findCriterion(Integer criterionId) {
    return evaluationCriterionRepository
        .findById(criterionId)
        .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found"));
  }
}
