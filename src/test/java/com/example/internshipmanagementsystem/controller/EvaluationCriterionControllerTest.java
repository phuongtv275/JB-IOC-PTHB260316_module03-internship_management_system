package com.example.internshipmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.dto.response.EvaluationCriterionResponse;
import com.example.internshipmanagementsystem.security.JwtAuthenticationFilter;
import com.example.internshipmanagementsystem.service.EvaluationCriterionService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EvaluationCriterionController.class)
@AutoConfigureMockMvc(addFilters = false)
class EvaluationCriterionControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private EvaluationCriterionService evaluationCriterionService;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldCreateCriterion_whenRequestIsValid() throws Exception {
    when(evaluationCriterionService.createCriterion(any()))
        .thenReturn(
            new EvaluationCriterionResponse(1, "Communication", null, new BigDecimal("10.00")));

    mockMvc
        .perform(
            post("/api/evaluation_criteria")
                .contentType("application/json")
                .content("{\"criterionName\":\"Communication\",\"maxScore\":10.00}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.criterionName").value("Communication"));
  }

  @Test
  void shouldRejectCriterion_whenMaxScoreIsNotPositive() throws Exception {
    mockMvc
        .perform(
            post("/api/evaluation_criteria")
                .contentType("application/json")
                .content("{\"criterionName\":\"Communication\",\"maxScore\":0}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT_DATA"));
  }

  @Test
  void shouldReadUpdateAndDeleteCriterion_whenRequestsAreValid() throws Exception {
    EvaluationCriterionResponse response =
        new EvaluationCriterionResponse(1, "Communication", null, new BigDecimal("10.00"));
    when(evaluationCriterionService.getCriterion(1)).thenReturn(response);
    when(evaluationCriterionService.updateCriterion(org.mockito.ArgumentMatchers.eq(1), any()))
        .thenReturn(response);

    mockMvc
        .perform(get("/api/evaluation_criteria/{criterionId}", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.criterionId").value(1));
    mockMvc
        .perform(
            put("/api/evaluation_criteria/{criterionId}", 1)
                .contentType("application/json")
                .content("{\"criterionName\":\"Communication\",\"maxScore\":10}"))
        .andExpect(status().isOk());
    mockMvc
        .perform(delete("/api/evaluation_criteria/{criterionId}", 1))
        .andExpect(status().isNoContent());
  }
}
