package com.example.internshipmanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

  private final HttpStatus status;
  private final String errorCode;

  protected BusinessException(HttpStatus status, String errorCode, String message) {
    super(message);
    this.status = status;
    this.errorCode = errorCode;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
