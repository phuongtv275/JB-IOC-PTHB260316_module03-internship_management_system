package com.example.internshipmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.security.JwtService;
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
class PhaseCriterionAuthorizationIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private MentorRepository mentorRepository;

  @MockitoBean private JwtService jwtService;

  @BeforeEach
  void setUp() {
    studentRepository.deleteAll();
    mentorRepository.deleteAll();
    userRepository.deleteAll();
    when(jwtService.extractUsername(anyString()))
        .thenAnswer(invocation -> invocation.getArgument(0));
    saveUser("admin", Role.ADMIN);
    saveUser("mentor", Role.MENTOR);
    saveUser("student", Role.STUDENT);
  }

  @Test
  void shouldAllowAllRolesToReadPhasesAndCriteria() throws Exception {
    mockMvc
        .perform(get("/api/internship_phases").header(HttpHeaders.AUTHORIZATION, "Bearer mentor"))
        .andExpect(status().isOk());
    mockMvc
        .perform(
            get("/api/evaluation_criteria").header(HttpHeaders.AUTHORIZATION, "Bearer student"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldRejectNonAdminWrites_whenCreatingPhaseOrCriterion() throws Exception {
    mockMvc
        .perform(
            post("/api/internship_phases")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mentor")
                .contentType("application/json")
                .content(
                    "{\"phaseName\":\"Phase 1\",\"startDate\":\"2026-09-01\",\"endDate\":\"2026-12-31\"}"))
        .andExpect(status().isForbidden());
    mockMvc
        .perform(
            post("/api/evaluation_criteria")
                .header(HttpHeaders.AUTHORIZATION, "Bearer student")
                .contentType("application/json")
                .content("{\"criterionName\":\"Communication\",\"maxScore\":10}"))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldAllowAdminToCreatePhaseAndCriterion() throws Exception {
    mockMvc
        .perform(
            post("/api/internship_phases")
                .header(HttpHeaders.AUTHORIZATION, "Bearer admin")
                .contentType("application/json")
                .content(
                    "{\"phaseName\":\"Phase 1\",\"startDate\":\"2026-09-01\",\"endDate\":\"2026-12-31\"}"))
        .andExpect(status().isCreated());
    mockMvc
        .perform(
            post("/api/evaluation_criteria")
                .header(HttpHeaders.AUTHORIZATION, "Bearer admin")
                .contentType("application/json")
                .content("{\"criterionName\":\"Communication\",\"maxScore\":10}"))
        .andExpect(status().isCreated());
  }

  private void saveUser(String username, Role role) {
    userRepository.save(
        User.create(username, "hash", username, username + "@example.com", null, role));
  }
}
