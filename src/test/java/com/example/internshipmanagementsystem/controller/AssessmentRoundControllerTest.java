package com.example.internshipmanagementsystem.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.security.JwtAuthenticationFilter;
import com.example.internshipmanagementsystem.service.AssessmentRoundService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AssessmentRoundController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssessmentRoundControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AssessmentRoundService assessmentRoundService;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldFilterRounds_whenPhaseIdUsesDocumentedQueryName() throws Exception {
    mockMvc
        .perform(get("/api/assessment_rounds").queryParam("phase_id", "7"))
        .andExpect(status().isOk());

    verify(assessmentRoundService).getRounds(7L);
  }
}
