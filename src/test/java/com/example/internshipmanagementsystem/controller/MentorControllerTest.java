package com.example.internshipmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.dto.response.MentorResponse;
import com.example.internshipmanagementsystem.security.JwtAuthenticationFilter;
import com.example.internshipmanagementsystem.service.MentorService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MentorController.class)
@AutoConfigureMockMvc(addFilters = false)
class MentorControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private MentorService mentorService;

  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldCreateMentor_whenRequestIsValid() throws Exception {
    when(mentorService.createMentor(any()))
        .thenReturn(
            new MentorResponse(1L, "Engineering", null, LocalDateTime.now(), LocalDateTime.now()));

    mockMvc
        .perform(
            post("/api/mentors")
                .contentType("application/json")
                .content("{\"mentorId\":1,\"department\":\"Engineering\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.department").value("Engineering"));
  }
}
