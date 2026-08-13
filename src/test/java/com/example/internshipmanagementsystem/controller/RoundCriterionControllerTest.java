package com.example.internshipmanagementsystem.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.security.JwtAuthenticationFilter;
import com.example.internshipmanagementsystem.service.RoundCriterionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RoundCriterionController.class)
@AutoConfigureMockMvc(addFilters = false)
class RoundCriterionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private RoundCriterionService roundCriterionService;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldFilterRoundCriteria_whenRoundIdUsesDocumentedQueryName() throws Exception {
    mockMvc
        .perform(get("/api/round_criteria").queryParam("round_id", "9"))
        .andExpect(status().isOk());

    verify(roundCriterionService).getRoundCriteria(9L);
  }
}
