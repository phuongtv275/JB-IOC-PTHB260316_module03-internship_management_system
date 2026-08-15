package com.example.internshipmanagementsystem.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

class JwtAuthenticationFilterTest {

  private final JwtService jwtService = mock(JwtService.class);
  private final DatabaseUserDetailsService userDetailsService =
      mock(DatabaseUserDetailsService.class);
  private final JwtAuthenticationFilter filter =
      new JwtAuthenticationFilter(jwtService, userDetailsService);

  @AfterEach
  void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldRejectToken_whenAccountIsDisabled() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    request.addHeader("Authorization", "Bearer valid-token");
    FilterChain filterChain = mock(FilterChain.class);
    when(jwtService.extractUsername("valid-token")).thenReturn("disabled-user");
    when(userDetailsService.loadUserByUsername("disabled-user"))
        .thenReturn(User.withUsername("disabled-user").password("hash").disabled(true).build());

    filter.doFilter(request, response, filterChain);

    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    assertThat(request.getAttribute(JwtAuthenticationFilter.JWT_ERROR_ATTRIBUTE)).isEqualTo(true);
    verify(filterChain).doFilter(request, response);
  }
}
