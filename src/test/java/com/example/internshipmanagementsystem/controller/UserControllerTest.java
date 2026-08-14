package com.example.internshipmanagementsystem.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.security.JwtAuthenticationFilter;
import com.example.internshipmanagementsystem.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private UserService userService;

  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldReturnUsers_whenRequestedByAdmin() throws Exception {
    UserResponse user =
        new UserResponse(
            1,
            "student",
            "Student",
            "student@example.com",
            null,
            Role.STUDENT,
            true,
            LocalDateTime.now(),
            LocalDateTime.now());
    when(userService.getUsers(null)).thenReturn(List.of(user));

    mockMvc
        .perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.content[0].username").value("student"))
        .andExpect(jsonPath("$.data.page").value(0))
        .andExpect(jsonPath("$.data.size").value(5));
  }

  @Test
  void shouldReturnInvalidInput_whenRoleQueryParameterIsUnknown() throws Exception {
    mockMvc
        .perform(get("/api/users").param("role", "UNKNOWN"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT_DATA"));
  }

  @Test
  void shouldReturnInvalidInput_whenRoleInRequestBodyIsUnknown() throws Exception {
    mockMvc
        .perform(
            post("/api/users")
                .contentType("application/json")
                .content(
                    """
                    {"username":"student","password":"password","fullName":"Student","email":"student@example.com","role":"UNKNOWN"}
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT_DATA"));
  }
}
