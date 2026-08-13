package com.example.internshipmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.dto.response.InternshipPhaseResponse;
import com.example.internshipmanagementsystem.exception.InvalidInternshipPhaseException;
import com.example.internshipmanagementsystem.security.JwtAuthenticationFilter;
import com.example.internshipmanagementsystem.service.InternshipPhaseService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InternshipPhaseController.class)
@AutoConfigureMockMvc(addFilters = false)
class InternshipPhaseControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private InternshipPhaseService internshipPhaseService;
  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldCreatePhase_whenRequestIsValid() throws Exception {
    when(internshipPhaseService.createPhase(any()))
        .thenReturn(
            new InternshipPhaseResponse(
                1, "Phase 1", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), null));

    mockMvc
        .perform(
            post("/api/internship_phases")
                .contentType("application/json")
                .content(
                    "{\"phaseName\":\"Phase 1\",\"startDate\":\"2026-09-01\",\"endDate\":\"2026-12-31\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.phaseName").value("Phase 1"));
  }

  @Test
  void shouldRejectPhase_whenRequiredFieldsAreMissing() throws Exception {
    mockMvc
        .perform(post("/api/internship_phases").contentType("application/json").content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT_DATA"));
  }

  @Test
  void shouldRejectPhase_whenEndDatePrecedesStartDate() throws Exception {
    when(internshipPhaseService.createPhase(any()))
        .thenThrow(
            new InvalidInternshipPhaseException("End date must be after or equal to start date"));

    mockMvc
        .perform(
            post("/api/internship_phases")
                .contentType("application/json")
                .content(
                    "{\"phaseName\":\"Phase 1\",\"startDate\":\"2026-09-01\",\"endDate\":\"2026-08-31\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("INVALID_INTERNSHIP_PHASE"));
  }

  @Test
  void shouldReadUpdateAndDeletePhase_whenRequestsAreValid() throws Exception {
    InternshipPhaseResponse response =
        new InternshipPhaseResponse(
            1, "Phase 1", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), null);
    when(internshipPhaseService.getPhase(1)).thenReturn(response);
    when(internshipPhaseService.updatePhase(org.mockito.ArgumentMatchers.eq(1), any()))
        .thenReturn(response);

    mockMvc
        .perform(get("/api/internship_phases/{phaseId}", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.phaseId").value(1));
    mockMvc
        .perform(
            put("/api/internship_phases/{phaseId}", 1)
                .contentType("application/json")
                .content(
                    "{\"phaseName\":\"Phase 1\",\"startDate\":\"2026-09-01\",\"endDate\":\"2026-12-31\"}"))
        .andExpect(status().isOk());
    mockMvc
        .perform(delete("/api/internship_phases/{phaseId}", 1))
        .andExpect(status().isNoContent());
  }
}
