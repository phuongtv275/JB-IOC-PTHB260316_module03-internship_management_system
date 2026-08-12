package com.example.internshipmanagementsystem.dto.response;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
    boolean success, int statusCode, String message, T data, OffsetDateTime timestamp) {

  public static <T> ApiResponse<T> success(int statusCode, String message, T data) {
    return new ApiResponse<>(true, statusCode, message, data, OffsetDateTime.now());
  }
}
