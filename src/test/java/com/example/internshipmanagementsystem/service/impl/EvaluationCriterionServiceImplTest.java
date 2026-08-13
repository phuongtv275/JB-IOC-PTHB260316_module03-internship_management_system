package com.example.internshipmanagementsystem.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.dto.request.EvaluationCriterionRequest;
import com.example.internshipmanagementsystem.entity.EvaluationCriterion;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.InvalidEvaluationCriterionException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.EvaluationCriterionMapper;
import com.example.internshipmanagementsystem.repository.EvaluationCriterionRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EvaluationCriterionServiceImplTest {

  private final EvaluationCriterionRepository evaluationCriterionRepository =
      Mockito.mock(EvaluationCriterionRepository.class);
  private final EvaluationCriterionServiceImpl evaluationCriterionService =
      new EvaluationCriterionServiceImpl(
          evaluationCriterionRepository, new EvaluationCriterionMapper());

  @Test
  void shouldThrowException_whenCriterionNameAlreadyExists() {
    EvaluationCriterionRequest request =
        new EvaluationCriterionRequest("Communication", null, new BigDecimal("10.00"));
    when(evaluationCriterionRepository.existsByCriterionName("Communication")).thenReturn(true);

    assertThatThrownBy(() -> evaluationCriterionService.createCriterion(request))
        .isInstanceOf(DuplicateResourceException.class);
  }

  @Test
  void shouldThrowException_whenMaximumScoreIsNotPositive() {
    EvaluationCriterionRequest request =
        new EvaluationCriterionRequest("Communication", null, BigDecimal.ZERO);

    assertThatThrownBy(() -> evaluationCriterionService.createCriterion(request))
        .isInstanceOf(InvalidEvaluationCriterionException.class);
  }

  @Test
  void shouldCreateCriterion_whenRequestIsValid() {
    EvaluationCriterionRequest request =
        new EvaluationCriterionRequest("Communication", null, new BigDecimal("10.00"));
    when(evaluationCriterionRepository.save(
            org.mockito.ArgumentMatchers.any(EvaluationCriterion.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    assertThat(evaluationCriterionService.createCriterion(request).criterionName())
        .isEqualTo("Communication");
  }

  @Test
  void shouldUpdateCriterion_whenRequestIsValid() {
    EvaluationCriterion criterion =
        EvaluationCriterion.create("Communication", null, new BigDecimal("10.00"));
    EvaluationCriterionRequest request =
        new EvaluationCriterionRequest("Teamwork", null, new BigDecimal("20.00"));
    when(evaluationCriterionRepository.findById(1L)).thenReturn(Optional.of(criterion));

    assertThat(evaluationCriterionService.updateCriterion(1L, request).criterionName())
        .isEqualTo("Teamwork");
  }

  @Test
  void shouldThrowException_whenCriterionDoesNotExist() {
    when(evaluationCriterionRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> evaluationCriterionService.getCriterion(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void shouldDeleteCriterion_whenCriterionExists() {
    EvaluationCriterion criterion =
        EvaluationCriterion.create("Communication", null, new BigDecimal("10.00"));
    when(evaluationCriterionRepository.findById(1L)).thenReturn(Optional.of(criterion));

    evaluationCriterionService.deleteCriterion(1L);
  }
}
