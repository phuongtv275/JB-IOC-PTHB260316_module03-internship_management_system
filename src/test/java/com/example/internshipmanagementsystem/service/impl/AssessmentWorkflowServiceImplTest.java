package com.example.internshipmanagementsystem.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.dto.request.AssessmentResultRequest;
import com.example.internshipmanagementsystem.entity.AssessmentRound;
import com.example.internshipmanagementsystem.entity.EvaluationCriterion;
import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import com.example.internshipmanagementsystem.entity.InternshipPhase;
import com.example.internshipmanagementsystem.entity.Mentor;
import com.example.internshipmanagementsystem.entity.Student;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.exception.InvalidAssessmentResultException;
import com.example.internshipmanagementsystem.repository.AssessmentResultRepository;
import com.example.internshipmanagementsystem.repository.AssessmentRoundRepository;
import com.example.internshipmanagementsystem.repository.EvaluationCriterionRepository;
import com.example.internshipmanagementsystem.repository.InternshipAssignmentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AssessmentWorkflowServiceImplTest {

  @Test
  void shouldRejectScoreAboveCriterionMaximum() {
    User mentorUser = TestAssessmentData.mentorUser("mentor");
    InternshipAssignment assignment = TestAssessmentData.assignment(mentorUser);
    AssessmentRound round = TestAssessmentData.round(assignment.getPhase());
    EvaluationCriterion criterion =
        EvaluationCriterion.create("Communication", null, BigDecimal.TEN);
    AssessmentResultRepository resultRepository = Mockito.mock(AssessmentResultRepository.class);
    InternshipAssignmentRepository assignmentRepository =
        Mockito.mock(InternshipAssignmentRepository.class);
    AssessmentRoundRepository roundRepository = Mockito.mock(AssessmentRoundRepository.class);
    EvaluationCriterionRepository criterionRepository =
        Mockito.mock(EvaluationCriterionRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
    when(roundRepository.findById(1L)).thenReturn(Optional.of(round));
    when(criterionRepository.findById(1L)).thenReturn(Optional.of(criterion));
    when(userRepository.findByUsername("mentor")).thenReturn(Optional.of(mentorUser));
    AssessmentResultServiceImpl service =
        new AssessmentResultServiceImpl(
            resultRepository,
            assignmentRepository,
            roundRepository,
            criterionRepository,
            roundCriterionRepository(),
            userRepository,
            new com.example.internshipmanagementsystem.mapper.AssessmentResultMapper());

    assertThatThrownBy(
            () ->
                service.createResult(
                    new AssessmentResultRequest(1L, 1L, 1L, BigDecimal.valueOf(11), null),
                    "mentor"))
        .isInstanceOf(InvalidAssessmentResultException.class);
  }

  @Test
  void shouldRejectMentorWhoDoesNotOwnAssignment() {
    User owner = TestAssessmentData.mentorUser("owner");
    InternshipAssignment assignment = TestAssessmentData.assignment(owner);
    InternshipAssignmentRepository assignmentRepository =
        Mockito.mock(InternshipAssignmentRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    User other = TestAssessmentData.mentorUser("other");
    when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
    when(userRepository.findByUsername("other")).thenReturn(Optional.of(other));
    AssessmentResultServiceImpl service =
        new AssessmentResultServiceImpl(
            Mockito.mock(AssessmentResultRepository.class),
            assignmentRepository,
            Mockito.mock(AssessmentRoundRepository.class),
            Mockito.mock(EvaluationCriterionRepository.class),
            Mockito.mock(
                com.example.internshipmanagementsystem.repository.RoundCriterionRepository.class),
            userRepository,
            new com.example.internshipmanagementsystem.mapper.AssessmentResultMapper());

    assertThatThrownBy(
            () ->
                service.createResult(
                    new AssessmentResultRequest(1L, 1L, 1L, BigDecimal.ONE, null), "other"))
        .isInstanceOf(AccessDeniedBusinessException.class);
  }

  @Test
  void shouldRejectUpdate_whenResultRoundNoLongerMatchesAssignmentPhase() {
    User mentor = TestAssessmentData.mentorUser("mentor");
    InternshipPhase assignmentPhase = Mockito.mock(InternshipPhase.class);
    InternshipPhase roundPhase = Mockito.mock(InternshipPhase.class);
    InternshipAssignment assignment = Mockito.mock(InternshipAssignment.class);
    AssessmentRound round = Mockito.mock(AssessmentRound.class);
    EvaluationCriterion criterion = Mockito.mock(EvaluationCriterion.class);
    com.example.internshipmanagementsystem.entity.AssessmentResult result =
        Mockito.mock(com.example.internshipmanagementsystem.entity.AssessmentResult.class);
    AssessmentResultRepository resultRepository = Mockito.mock(AssessmentResultRepository.class);
    when(assignmentPhase.getPhaseId()).thenReturn(1L);
    when(roundPhase.getPhaseId()).thenReturn(2L);
    when(assignment.getAssignmentId()).thenReturn(1L);
    when(assignment.getPhase()).thenReturn(assignmentPhase);
    when(assignment.getMentor()).thenReturn(Mentor.create(mentor, null, null));
    when(round.getRoundId()).thenReturn(1L);
    when(round.getPhase()).thenReturn(roundPhase);
    when(criterion.getCriterionId()).thenReturn(1L);
    when(criterion.getMaxScore()).thenReturn(BigDecimal.TEN);
    when(result.getAssignment()).thenReturn(assignment);
    when(result.getRound()).thenReturn(round);
    when(result.getCriterion()).thenReturn(criterion);
    when(resultRepository.findById(1L)).thenReturn(Optional.of(result));
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    when(userRepository.findByUsername("mentor")).thenReturn(Optional.of(mentor));
    AssessmentResultServiceImpl service =
        new AssessmentResultServiceImpl(
            resultRepository,
            Mockito.mock(InternshipAssignmentRepository.class),
            Mockito.mock(AssessmentRoundRepository.class),
            Mockito.mock(EvaluationCriterionRepository.class),
            Mockito.mock(
                com.example.internshipmanagementsystem.repository.RoundCriterionRepository.class),
            userRepository,
            new com.example.internshipmanagementsystem.mapper.AssessmentResultMapper());

    assertThatThrownBy(
            () ->
                service.updateResult(
                    1L, new AssessmentResultRequest(1L, 1L, 1L, BigDecimal.ONE, null), "mentor"))
        .isInstanceOf(InvalidAssessmentResultException.class);
  }

  static final class TestAssessmentData {
    private TestAssessmentData() {}

    static User mentorUser(String username) {
      User user = Mockito.mock(User.class);
      when(user.getUsername()).thenReturn(username);
      when(user.getRole()).thenReturn(com.example.internshipmanagementsystem.entity.Role.MENTOR);
      when(user.getUserId()).thenReturn((long) username.hashCode());
      return user;
    }

    static InternshipAssignment assignment(User mentorUser) {
      InternshipPhase phase =
          InternshipPhase.create(
              "Phase", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), null);
      return InternshipAssignment.create(
          Student.create(studentUser(), "S1", null, null, null, null),
          Mentor.create(mentorUser, null, null),
          phase);
    }

    static AssessmentRound round(InternshipPhase phase) {
      return AssessmentRound.create(
          phase, "Round", LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 2), null);
    }

    static User studentUser() {
      User user = Mockito.mock(User.class);
      when(user.getRole()).thenReturn(com.example.internshipmanagementsystem.entity.Role.STUDENT);
      return user;
    }
  }

  private static com.example.internshipmanagementsystem.repository.RoundCriterionRepository
      roundCriterionRepository() {
    com.example.internshipmanagementsystem.repository.RoundCriterionRepository repository =
        Mockito.mock(
            com.example.internshipmanagementsystem.repository.RoundCriterionRepository.class);
    when(repository.existsByRoundRoundIdAndCriterionCriterionId(Mockito.any(), Mockito.any()))
        .thenReturn(true);
    return repository;
  }
}
