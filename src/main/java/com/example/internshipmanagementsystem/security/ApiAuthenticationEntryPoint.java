package com.example.internshipmanagementsystem.security;

import com.example.internshipmanagementsystem.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    boolean invalidToken =
        request.getAttribute(JwtAuthenticationFilter.JWT_ERROR_ATTRIBUTE) != null;
    boolean accountDisabled =
        request.getAttribute(JwtAuthenticationFilter.ACCOUNT_DISABLED_ATTRIBUTE) != null;
    response.setStatus(
        accountDisabled ? HttpServletResponse.SC_FORBIDDEN : HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    objectMapper.writeValue(
        response.getOutputStream(),
        ErrorResponse.of(
            accountDisabled ? 403 : 401,
            accountDisabled
                ? "ACCOUNT_DISABLED"
                : invalidToken ? "INVALID_JWT_TOKEN" : "BAD_CREDENTIALS",
            accountDisabled
                ? "Tài khoản hiện tại đã bị vô hiệu hóa. Liên hệ với mentor hoặc quản lý nhân sự để kích hoạt lại tài khoản"
                : "Unauthorized",
            List.of()));
  }
}
