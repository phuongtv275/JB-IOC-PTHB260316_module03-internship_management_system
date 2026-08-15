package com.example.internshipmanagementsystem.exception;

import com.example.internshipmanagementsystem.dto.response.ErrorResponse;
import com.example.internshipmanagementsystem.dto.response.FieldErrorResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
    return response(
        exception.getStatus(), exception.getErrorCode(), exception.getMessage(), List.of());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException exception) {
    List<FieldErrorResponse> errors =
        exception.getBindingResult().getFieldErrors().stream().map(this::toFieldError).toList();
    return response(HttpStatus.BAD_REQUEST, "INVALID_INPUT_DATA", "Invalid input data", errors);
  }

  @ExceptionHandler({
    MethodArgumentTypeMismatchException.class,
    HttpMessageNotReadableException.class
  })
  ResponseEntity<ErrorResponse> handleUnreadableInput(Exception exception) {
    return response(HttpStatus.BAD_REQUEST, "INVALID_INPUT_DATA", "Invalid input data", List.of());
  }

  @ExceptionHandler(AuthenticationException.class)
  ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception) {
    return response(
        HttpStatus.UNAUTHORIZED, "BAD_CREDENTIALS", "Invalid username or password", List.of());
  }

  @ExceptionHandler(DisabledException.class)
  ResponseEntity<ErrorResponse> handleDisabledAccountException(DisabledException exception) {
    return response(
        HttpStatus.UNAUTHORIZED,
        "ACCOUNT_DISABLED",
        "Tài khoản hiện tại đã bị vô hiệu hóa. Liên hệ với mentor hoặc quản lý nhân sự để kích hoạt lại tài khoản",
        List.of());
  }

  @ExceptionHandler(AccessDeniedException.class)
  ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
    return response(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "Access denied", List.of());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException exception) {
    return response(
        HttpStatus.CONFLICT,
        "RESOURCE_CONFLICT",
        "Cannot modify resource because related records exist",
        List.of());
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception) {
    log.error("IMS_ERROR STATUS=500 ERROR_CODE=INTERNAL_SERVER_ERROR", exception);
    return response(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "INTERNAL_SERVER_ERROR",
        "Internal server error",
        List.of());
  }

  private FieldErrorResponse toFieldError(FieldError error) {
    return new FieldErrorResponse(error.getField(), error.getDefaultMessage());
  }

  private ResponseEntity<ErrorResponse> response(
      HttpStatus status, String errorCode, String message, List<FieldErrorResponse> errors) {
    return ResponseEntity.status(status)
        .body(ErrorResponse.of(status.value(), errorCode, message, errors));
  }
}
