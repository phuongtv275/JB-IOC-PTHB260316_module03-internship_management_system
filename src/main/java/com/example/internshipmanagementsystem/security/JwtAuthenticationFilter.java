package com.example.internshipmanagementsystem.security;

import com.example.internshipmanagementsystem.config.TraceIdFilter;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  static final String JWT_ERROR_ATTRIBUTE = "jwtError";
  static final String ACCOUNT_DISABLED_ATTRIBUTE = "accountDisabled";
  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtService jwtService;
  private final DatabaseUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith(BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }
    try {
      String username = jwtService.extractUsername(header.substring(BEARER_PREFIX.length()));
      if (SecurityContextHolder.getContext().getAuthentication() == null) {
        authenticate(request, username);
      }
    } catch (JwtException | IllegalArgumentException exception) {
      request.setAttribute(JWT_ERROR_ATTRIBUTE, Boolean.TRUE);
    }
    filterChain.doFilter(request, response);
  }

  private void authenticate(HttpServletRequest request, String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if (!userDetails.isEnabled()) {
      request.setAttribute(ACCOUNT_DISABLED_ATTRIBUTE, Boolean.TRUE);
      return;
    }
    UsernamePasswordAuthenticationToken authentication =
        UsernamePasswordAuthenticationToken.authenticated(
            userDetails, null, userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    request.setAttribute(
        TraceIdFilter.AUTHENTICATED_ROLE_ATTRIBUTE,
        userDetails.getAuthorities().stream()
            .findFirst()
            .map(authority -> authority.getAuthority().replace("ROLE_", ""))
            .orElse("AUTHENTICATED"));
  }
}
