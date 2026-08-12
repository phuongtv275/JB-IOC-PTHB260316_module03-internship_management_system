package com.example.internshipmanagementsystem.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.dto.request.LoginRequest;
import com.example.internshipmanagementsystem.dto.response.LoginResponse;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

class AuthServiceImplTest {

  private final AuthenticationManager authenticationManager =
      Mockito.mock(AuthenticationManager.class);
  private final JwtService jwtService = Mockito.mock(JwtService.class);
  private final AuthServiceImpl authService =
      new AuthServiceImpl(authenticationManager, jwtService);

  @Test
  void shouldReturnBearerToken_whenCredentialsAreValid() {
    User principal = new User("admin", "hash", java.util.List.of(() -> "ROLE_" + Role.ADMIN));
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
    when(jwtService.generateToken(principal)).thenReturn("token");

    LoginResponse response = authService.login(new LoginRequest("admin", "password"));

    assertThat(response.accessToken()).isEqualTo("token");
    assertThat(response.tokenType()).isEqualTo("Bearer");
  }
}
