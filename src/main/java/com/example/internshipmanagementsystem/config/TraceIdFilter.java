package com.example.internshipmanagementsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TraceIdFilter extends OncePerRequestFilter {

  private static final String TRACE_ID = "traceId";
  public static final String AUTHENTICATED_ROLE_ATTRIBUTE = "authenticatedRole";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String traceId = UUID.randomUUID().toString();
    long startTime = System.nanoTime();
    MDC.put(TRACE_ID, traceId);
    try {
      filterChain.doFilter(request, response);
    } finally {
      log.info(
          "IMS_REQUEST METHOD={} PATH={} STATUS={} DURATION_MS={} ROLE={} TRACE_ID={}",
          request.getMethod(),
          request.getRequestURI(),
          response.getStatus(),
          (System.nanoTime() - startTime) / 1_000_000,
          getRole(request),
          traceId);
      MDC.remove(TRACE_ID);
    }
  }

  private String getRole(HttpServletRequest request) {
    Object authenticatedRole = request.getAttribute(AUTHENTICATED_ROLE_ATTRIBUTE);
    if (authenticatedRole instanceof String role) {
      return role;
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return "ANONYMOUS";
    }
    return authentication.getAuthorities().stream()
        .findFirst()
        .map(authority -> authority.getAuthority().replace("ROLE_", ""))
        .orElse("AUTHENTICATED");
  }
}
