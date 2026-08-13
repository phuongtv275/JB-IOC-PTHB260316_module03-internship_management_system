package com.example.internshipmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.entity.AssessmentResult;
import com.example.internshipmanagementsystem.entity.AssessmentRound;
import com.example.internshipmanagementsystem.entity.EvaluationCriterion;
import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import com.example.internshipmanagementsystem.entity.InternshipPhase;
import com.example.internshipmanagementsystem.entity.Mentor;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.RoundCriterion;
import com.example.internshipmanagementsystem.entity.Student;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.repository.AssessmentResultRepository;
import com.example.internshipmanagementsystem.repository.AssessmentRoundRepository;
import com.example.internshipmanagementsystem.repository.EvaluationCriterionRepository;
import com.example.internshipmanagementsystem.repository.InternshipAssignmentRepository;
import com.example.internshipmanagementsystem.repository.InternshipPhaseRepository;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.RoundCriterionRepository;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.security.JwtService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AssessmentWorkflowAuthorizationIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private MentorRepository mentorRepository;
  @Autowired private InternshipPhaseRepository internshipPhaseRepository;
  @Autowired private EvaluationCriterionRepository evaluationCriterionRepository;
  @Autowired private AssessmentRoundRepository assessmentRoundRepository;
  @Autowired private RoundCriterionRepository roundCriterionRepository;
  @Autowired private InternshipAssignmentRepository internshipAssignmentRepository;
  @Autowired private AssessmentResultRepository assessmentResultRepository;

  @MockitoBean private JwtService jwtService;

  private InternshipAssignment assignment;
  private AssessmentRound round;
  private EvaluationCriterion criterion;

  @BeforeEach
  void setUp() {
    clearData();
    when(jwtService.extractUsername(anyString()))
        .thenAnswer(invocation -> invocation.getArgument(0));
    User studentUser = saveUser("student", Role.STUDENT);
    User mentorUser = saveUser("mentor", Role.MENTOR);
    saveUser("other-mentor", Role.MENTOR);
    Student student =
        studentRepository.save(Student.create(studentUser, "SV001", null, null, null, null));
    Mentor mentor = mentorRepository.save(Mentor.create(mentorUser, null, null));
    mentorRepository.save(
        Mentor.create(userRepository.findByUsername("other-mentor").orElseThrow(), null, null));
    InternshipPhase phase =
        internshipPhaseRepository.save(
            InternshipPhase.create(
                "Phase", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31), null));
    criterion =
        evaluationCriterionRepository.save(
            EvaluationCriterion.create("Communication", null, BigDecimal.TEN));
    round =
        assessmentRoundRepository.save(
            AssessmentRound.create(
                phase, "Round", LocalDate.of(2026, 2, 1), LocalDate.of(2026, 2, 2), null));
    roundCriterionRepository.save(RoundCriterion.create(round, criterion, BigDecimal.ONE));
    assignment =
        internshipAssignmentRepository.save(InternshipAssignment.create(student, mentor, phase));
  }

  @AfterEach
  void tearDown() {
    clearData();
  }

  @Test
  void shouldRestrictAssessmentRecordsToAssignedMentorAndStudent() throws Exception {
    mockMvc
        .perform(
            get("/api/internship_assignments/{assignmentId}", assignment.getAssignmentId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer other-mentor"))
        .andExpect(status().isForbidden());
    mockMvc
        .perform(
            get("/api/internship_assignments").header(HttpHeaders.AUTHORIZATION, "Bearer student"))
        .andExpect(status().isOk());
    String body =
        "{\"assignmentId\":%d,\"roundId\":%d,\"criterionId\":%d,\"score\":8}"
            .formatted(
                assignment.getAssignmentId(), round.getRoundId(), criterion.getCriterionId());
    mockMvc
        .perform(
            post("/api/assessment_results")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mentor")
                .contentType("application/json")
                .content(body))
        .andExpect(status().isCreated());
    AssessmentResult result = assessmentResultRepository.findAll().get(0);
    mockMvc
        .perform(
            put("/api/assessment_results/{resultId}", result.getResultId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer other-mentor")
                .contentType("application/json")
                .content(body))
        .andExpect(status().isForbidden());
    mockMvc
        .perform(
            post("/api/assessment_results")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mentor")
                .contentType("application/json")
                .content(body))
        .andExpect(status().isBadRequest());
  }

  private User saveUser(String username, Role role) {
    return userRepository.save(
        User.create(username, "hash", username, username + "@example.com", null, role));
  }

  private void clearData() {
    assessmentResultRepository.deleteAll();
    roundCriterionRepository.deleteAll();
    internshipAssignmentRepository.deleteAll();
    assessmentRoundRepository.deleteAll();
    evaluationCriterionRepository.deleteAll();
    internshipPhaseRepository.deleteAll();
    studentRepository.deleteAll();
    mentorRepository.deleteAll();
    userRepository.deleteAll();
  }
}
