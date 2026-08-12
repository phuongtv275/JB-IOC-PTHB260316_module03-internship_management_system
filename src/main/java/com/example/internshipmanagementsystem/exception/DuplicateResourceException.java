package com.example.internshipmanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BusinessException {

  public DuplicateResourceException(String message) {
    super(HttpStatus.BAD_REQUEST, "DUPLICATE_RESOURCE", message);
  }
}
