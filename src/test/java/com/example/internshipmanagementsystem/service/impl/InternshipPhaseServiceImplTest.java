package com.example.internshipmanagementsystem.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.dto.request.InternshipPhaseRequest;
import com.example.internshipmanagementsystem.entity.InternshipPhase;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.InvalidInternshipPhaseException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.InternshipPhaseMapper;
import com.example.internshipmanagementsystem.repository.InternshipPhaseRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InternshipPhaseServiceImplTest {

  private final InternshipPhaseRepository internshipPhaseRepository =
      Mockito.mock(InternshipPhaseRepository.class);
  private final InternshipPhaseServiceImpl internshipPhaseService =
      new InternshipPhaseServiceImpl(internshipPhaseRepository, new InternshipPhaseMapper());

  @Test
  void shouldThrowException_whenPhaseEndDateIsBeforeStartDate() {
    InternshipPhaseRequest request =
        new InternshipPhaseRequest(
            "Phase 1", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 8, 31), null);

    assertThatThrownBy(() -> internshipPhaseService.createPhase(request))
        .isInstanceOf(InvalidInternshipPhaseException.class);
  }

  @Test
  void shouldCreatePhase_whenRequestIsValid() {
    InternshipPhaseRequest request =
        new InternshipPhaseRequest(
            "Phase 1", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), null);
    when(internshipPhaseRepository.save(org.mockito.ArgumentMatchers.any(InternshipPhase.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    assertThat(internshipPhaseService.createPhase(request).phaseName()).isEqualTo("Phase 1");
  }

  @Test
  void shouldUpdatePhase_whenRequestIsValid() {
    InternshipPhase phase =
        InternshipPhase.create(
            "Phase 1", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), null);
    InternshipPhaseRequest request =
        new InternshipPhaseRequest(
            "Phase 2", LocalDate.of(2027, 1, 1), LocalDate.of(2027, 4, 30), null);
    when(internshipPhaseRepository.findById(1)).thenReturn(Optional.of(phase));

    assertThat(internshipPhaseService.updatePhase(1, request).phaseName()).isEqualTo("Phase 2");
  }

  @Test
  void shouldThrowException_whenPhaseNameAlreadyExists() {
    InternshipPhaseRequest request =
        new InternshipPhaseRequest(
            "Phase 1", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), null);
    when(internshipPhaseRepository.existsByPhaseName("Phase 1")).thenReturn(true);

    assertThatThrownBy(() -> internshipPhaseService.createPhase(request))
        .isInstanceOf(DuplicateResourceException.class);
  }

  @Test
  void shouldThrowException_whenPhaseDoesNotExist() {
    when(internshipPhaseRepository.findById(1)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> internshipPhaseService.getPhase(1))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void shouldDeletePhase_whenPhaseExists() {
    InternshipPhase phase =
        InternshipPhase.create(
            "Phase 1", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), null);
    when(internshipPhaseRepository.findById(1)).thenReturn(Optional.of(phase));

    internshipPhaseService.deletePhase(1);
  }
}
