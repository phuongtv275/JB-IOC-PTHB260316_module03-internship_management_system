package com.example.internshipmanagementsystem.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.security.JwtAuthenticationFilter;
import com.example.internshipmanagementsystem.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
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
    when(userService.getUsers(eq(null), any(Pageable.class)))
        .thenReturn(
            new PageResponse<>(List.of(user), 0, 5, 1, 1, true, true, List.of("userId,asc")));

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

  @Test
  void shouldApplyStableSortAndMaximumSize_whenUsersAreRequested() throws Exception {
    when(userService.getUsers(eq(null), any(Pageable.class)))
        .thenReturn(new PageResponse<>(List.of(), 0, 100, 0, 0, true, true, List.of("userId,asc")));

    mockMvc.perform(get("/api/users").param("size", "1000")).andExpect(status().isOk());

    ArgumentCaptor<Pageable> pageable = ArgumentCaptor.forClass(Pageable.class);
    verify(userService).getUsers(eq(null), pageable.capture());
    assertThat(pageable.getValue().getPageSize()).isEqualTo(100);
    assertThat(pageable.getValue().getSort().getOrderFor("userId")).isNotNull();
  }
}
