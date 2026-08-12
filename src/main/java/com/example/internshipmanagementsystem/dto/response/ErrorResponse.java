package com.example.internshipmanagementsystem.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(
    boolean success,
    int statusCode,
    String errorCode,
    String message,
    List<FieldErrorResponse> errors,
    OffsetDateTime timestamp) {

  public static ErrorResponse of(
      int statusCode, String errorCode, String message, List<FieldErrorResponse> errors) {
    return new ErrorResponse(false, statusCode, errorCode, message, errors, OffsetDateTime.now());
  }
}
