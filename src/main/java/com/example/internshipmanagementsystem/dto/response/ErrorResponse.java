package com.example.internshipmanagementsystem.dto.response;

import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.MDC;

public record ErrorResponse(
    boolean success,
    int statusCode,
    String errorCode,
    String message,
    List<FieldErrorResponse> errors,
    String traceId,
    OffsetDateTime timestamp) {

  public static ErrorResponse of(
      int statusCode, String errorCode, String message, List<FieldErrorResponse> errors) {
    return new ErrorResponse(
        false, statusCode, errorCode, message, errors, MDC.get("traceId"), OffsetDateTime.now());
  }
}
