package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.AssessmentResultRequest;
import com.example.internshipmanagementsystem.dto.response.AssessmentResultResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.entity.AssessmentResult;
import com.example.internshipmanagementsystem.entity.AssessmentRound;
import com.example.internshipmanagementsystem.entity.EvaluationCriterion;
import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.InvalidAssessmentResultException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.AssessmentResultMapper;
import com.example.internshipmanagementsystem.repository.AssessmentResultRepository;
import com.example.internshipmanagementsystem.repository.AssessmentRoundRepository;
import com.example.internshipmanagementsystem.repository.EvaluationCriterionRepository;
import com.example.internshipmanagementsystem.repository.InternshipAssignmentRepository;
import com.example.internshipmanagementsystem.repository.RoundCriterionRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.service.AssessmentResultService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AssessmentResultServiceImpl implements AssessmentResultService {

  private final AssessmentResultRepository assessmentResultRepository;
  private final InternshipAssignmentRepository internshipAssignmentRepository;
  private final AssessmentRoundRepository assessmentRoundRepository;
  private final EvaluationCriterionRepository evaluationCriterionRepository;
  private final RoundCriterionRepository roundCriterionRepository;
  private final UserRepository userRepository;
  private final AssessmentResultMapper assessmentResultMapper;

  @Override
  public PageResponse<AssessmentResultResponse> getResults(
      String actorUsername, Pageable pageable) {
    User actor = findUser(actorUsername);
    Page<AssessmentResult> results =
        switch (actor.getRole()) {
          case ADMIN -> assessmentResultRepository.findAll(pageable);
          case MENTOR ->
              assessmentResultRepository.findByAssignmentMentorUserUsername(
                  actorUsername, pageable);
          case STUDENT ->
              assessmentResultRepository.findByAssignmentStudentUserUsername(
                  actorUsername, pageable);
        };
    return PageResponse.from(results.map(assessmentResultMapper::toResponse));
  }

  @Override
  @Transactional
  public AssessmentResultResponse createResult(
      AssessmentResultRequest request, String actorUsername) {
    User evaluator = findUser(actorUsername);
    requireMentor(evaluator);
    InternshipAssignment assignment = findAssignment(request.assignmentId());
    assertMentorOwnsAssignment(assignment, evaluator);
    AssessmentRound round = findRound(request.roundId());
    EvaluationCriterion criterion = findCriterion(request.criterionId());
    validateScore(request.score(), criterion.getMaxScore());
    validateRelationship(assignment, round, criterion);
    if (assessmentResultRepository
        .existsByAssignmentAssignmentIdAndRoundRoundIdAndCriterionCriterionId(
            request.assignmentId(), request.roundId(), request.criterionId())) {
      throw new DuplicateResourceException("Assessment result already exists");
    }
    AssessmentResult result =
        AssessmentResult.create(
            assignment, round, criterion, request.score(), request.comments(), evaluator);
    AssessmentResultResponse response =
        assessmentResultMapper.toResponse(assessmentResultRepository.save(result));
    log.info(
        "IMS_EVENT ASSESSMENT_RESULT_CREATED RESULT_ID={} ASSIGNMENT_ID={} ROUND_ID={} CRITERION_ID={}",
        response.resultId(),
        response.assignmentId(),
        response.roundId(),
        response.criterionId());
    return response;
  }

  @Override
  @Transactional
  public AssessmentResultResponse updateResult(
      Integer resultId, AssessmentResultRequest request, String actorUsername) {
    User evaluator = findUser(actorUsername);
    requireMentor(evaluator);
    AssessmentResult result = findResult(resultId);
    assertMentorOwnsAssignment(result.getAssignment(), evaluator);
    if (!result.getAssignment().getAssignmentId().equals(request.assignmentId())
        || !result.getRound().getRoundId().equals(request.roundId())
        || !result.getCriterion().getCriterionId().equals(request.criterionId())) {
      throw new InvalidAssessmentResultException(
          "Assessment result relationship cannot be changed");
    }
    validateAssignmentPhase(result.getAssignment(), result.getRound());
    validateScore(request.score(), result.getCriterion().getMaxScore());
    result.update(request.score(), request.comments());
    return assessmentResultMapper.toResponse(result);
  }

  private void validateRelationship(
      InternshipAssignment assignment, AssessmentRound round, EvaluationCriterion criterion) {
    validateAssignmentPhase(assignment, round);
    if (!roundCriterionRepository.existsByRoundRoundIdAndCriterionCriterionId(
        round.getRoundId(), criterion.getCriterionId())) {
      throw new InvalidAssessmentResultException("Criterion does not belong to assessment round");
    }
  }

  private void validateAssignmentPhase(InternshipAssignment assignment, AssessmentRound round) {
    if (!assignment.getPhase().getPhaseId().equals(round.getPhase().getPhaseId())) {
      throw new InvalidAssessmentResultException(
          "Assessment round must belong to assignment phase");
    }
  }

  private void validateScore(BigDecimal score, BigDecimal maxScore) {
    if (score.signum() < 0 || score.compareTo(maxScore) > 0) {
      throw new InvalidAssessmentResultException(
          "Score must be between zero and criterion maximum");
    }
  }

  private void requireMentor(User user) {
    if (user.getRole() != Role.MENTOR) {
      throw new AccessDeniedBusinessException("Only mentors can assess students");
    }
  }

  private void assertMentorOwnsAssignment(InternshipAssignment assignment, User mentor) {
    if (!assignment.getMentor().getUser().getUserId().equals(mentor.getUserId())) {
      throw new AccessDeniedBusinessException("Mentors can only assess their assignments");
    }
  }

  private AssessmentResult findResult(Integer resultId) {
    return assessmentResultRepository
        .findById(resultId)
        .orElseThrow(() -> new ResourceNotFoundException("Assessment result not found"));
  }

  private InternshipAssignment findAssignment(Integer assignmentId) {
    return internshipAssignmentRepository
        .findById(assignmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Internship assignment not found"));
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

  private User findUser(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }
}
