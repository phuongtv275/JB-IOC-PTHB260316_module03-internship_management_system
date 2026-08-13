package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.EvaluationCriterionRequest;
import com.example.internshipmanagementsystem.dto.response.EvaluationCriterionResponse;
import com.example.internshipmanagementsystem.entity.EvaluationCriterion;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.InvalidEvaluationCriterionException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.EvaluationCriterionMapper;
import com.example.internshipmanagementsystem.repository.EvaluationCriterionRepository;
import com.example.internshipmanagementsystem.service.EvaluationCriterionService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EvaluationCriterionServiceImpl implements EvaluationCriterionService {

  private final EvaluationCriterionRepository evaluationCriterionRepository;
  private final EvaluationCriterionMapper evaluationCriterionMapper;

  @Override
  public List<EvaluationCriterionResponse> getCriteria() {
    return evaluationCriterionRepository.findAll().stream()
        .map(evaluationCriterionMapper::toResponse)
        .toList();
  }

  @Override
  public EvaluationCriterionResponse getCriterion(Long criterionId) {
    return evaluationCriterionMapper.toResponse(findCriterion(criterionId));
  }

  @Override
  @Transactional
  public EvaluationCriterionResponse createCriterion(EvaluationCriterionRequest request) {
    validateMaximumScore(request.maxScore());
    if (evaluationCriterionRepository.existsByCriterionName(request.criterionName())) {
      throw new DuplicateResourceException("Evaluation criterion name already exists");
    }
    EvaluationCriterion criterion =
        EvaluationCriterion.create(
            request.criterionName(), request.description(), request.maxScore());
    return evaluationCriterionMapper.toResponse(evaluationCriterionRepository.save(criterion));
  }

  @Override
  @Transactional
  public EvaluationCriterionResponse updateCriterion(
      Long criterionId, EvaluationCriterionRequest request) {
    validateMaximumScore(request.maxScore());
    EvaluationCriterion criterion = findCriterion(criterionId);
    if (evaluationCriterionRepository.existsByCriterionNameAndCriterionIdNot(
        request.criterionName(), criterionId)) {
      throw new DuplicateResourceException("Evaluation criterion name already exists");
    }
    criterion.update(request.criterionName(), request.description(), request.maxScore());
    return evaluationCriterionMapper.toResponse(criterion);
  }

  @Override
  @Transactional
  public void deleteCriterion(Long criterionId) {
    evaluationCriterionRepository.delete(findCriterion(criterionId));
  }

  private EvaluationCriterion findCriterion(Long criterionId) {
    return evaluationCriterionRepository
        .findById(criterionId)
        .orElseThrow(() -> new ResourceNotFoundException("Evaluation criterion not found"));
  }

  private void validateMaximumScore(BigDecimal maxScore) {
    if (maxScore == null || maxScore.signum() <= 0) {
      throw new InvalidEvaluationCriterionException("Maximum score must be greater than zero");
    }
  }
}
