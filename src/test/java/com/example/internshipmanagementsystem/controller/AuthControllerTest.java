package com.example.internshipmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.dto.response.LoginResponse;
import com.example.internshipmanagementsystem.dto.response.UserSummaryResponse;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.security.JwtAuthenticationFilter;
import com.example.internshipmanagementsystem.service.AuthService;
import com.example.internshipmanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.DisabledException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AuthService authService;

  @MockitoBean private UserService userService;

  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldReturnToken_whenLoginRequestIsValid() throws Exception {
    when(authService.login(any()))
        .thenReturn(
            new LoginResponse(
                "token", "Bearer", new UserSummaryResponse(1, "admin", "Admin", Role.ADMIN, true)));

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"admin\",\"password\":\"password\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.accessToken").value("token"));
  }

  @Test
  void shouldReturnDisabledAccountMessage_whenAccountIsInactive() throws Exception {
    when(authService.login(any())).thenThrow(new DisabledException("Account is disabled"));

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"inactive\",\"password\":\"password\"}"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.errorCode").value("ACCOUNT_DISABLED"))
        .andExpect(
            jsonPath("$.message")
                .value(
                    "Tài khoản hiện tại đã bị vô hiệu hóa. Liên hệ với mentor hoặc quản lý nhân sự để kích hoạt lại tài khoản"));
  }
}
