package com.example.internshipmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.dto.response.StudentResponse;
import com.example.internshipmanagementsystem.security.JwtAuthenticationFilter;
import com.example.internshipmanagementsystem.service.StudentService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private StudentService studentService;

  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldCreateStudent_whenRequestIsValid() throws Exception {
    when(studentService.createStudent(any()))
        .thenReturn(
            new StudentResponse(
                1L, "SV001", null, null, null, null, LocalDateTime.now(), LocalDateTime.now()));

    mockMvc
        .perform(
            post("/api/students")
                .contentType("application/json")
                .content("{\"studentId\":1,\"studentCode\":\"SV001\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.studentCode").value("SV001"));
  }
}
