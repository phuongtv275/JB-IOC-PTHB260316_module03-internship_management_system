package com.example.internshipmanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedBusinessException extends BusinessException {

  public AccessDeniedBusinessException(String message) {
    super(HttpStatus.FORBIDDEN, "ACCESS_DENIED", message);
  }
}
