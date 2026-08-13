package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.InternshipPhaseRequest;
import com.example.internshipmanagementsystem.dto.response.InternshipPhaseResponse;
import com.example.internshipmanagementsystem.entity.InternshipPhase;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.InvalidInternshipPhaseException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.InternshipPhaseMapper;
import com.example.internshipmanagementsystem.repository.InternshipPhaseRepository;
import com.example.internshipmanagementsystem.service.InternshipPhaseService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InternshipPhaseServiceImpl implements InternshipPhaseService {

  private final InternshipPhaseRepository internshipPhaseRepository;
  private final InternshipPhaseMapper internshipPhaseMapper;

  @Override
  public List<InternshipPhaseResponse> getPhases() {
    return internshipPhaseRepository.findAll().stream()
        .map(internshipPhaseMapper::toResponse)
        .toList();
  }

  @Override
  public InternshipPhaseResponse getPhase(Integer phaseId) {
    return internshipPhaseMapper.toResponse(findPhase(phaseId));
  }

  @Override
  @Transactional
  public InternshipPhaseResponse createPhase(InternshipPhaseRequest request) {
    validateDateRange(request.startDate(), request.endDate());
    if (internshipPhaseRepository.existsByPhaseName(request.phaseName())) {
      throw new DuplicateResourceException("Internship phase name already exists");
    }
    InternshipPhase phase =
        InternshipPhase.create(
            request.phaseName(), request.startDate(), request.endDate(), request.description());
    return internshipPhaseMapper.toResponse(internshipPhaseRepository.save(phase));
  }

  @Override
  @Transactional
  public InternshipPhaseResponse updatePhase(Integer phaseId, InternshipPhaseRequest request) {
    validateDateRange(request.startDate(), request.endDate());
    InternshipPhase phase = findPhase(phaseId);
    if (internshipPhaseRepository.existsByPhaseNameAndPhaseIdNot(request.phaseName(), phaseId)) {
      throw new DuplicateResourceException("Internship phase name already exists");
    }
    phase.update(
        request.phaseName(), request.startDate(), request.endDate(), request.description());
    return internshipPhaseMapper.toResponse(phase);
  }

  @Override
  @Transactional
  public void deletePhase(Integer phaseId) {
    internshipPhaseRepository.delete(findPhase(phaseId));
  }

  private InternshipPhase findPhase(Integer phaseId) {
    return internshipPhaseRepository
        .findById(phaseId)
        .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found"));
  }

  private void validateDateRange(LocalDate startDate, LocalDate endDate) {
    if (endDate.isBefore(startDate)) {
      throw new InvalidInternshipPhaseException("End date must be after or equal to start date");
    }
  }
}
