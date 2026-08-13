package com.example.internshipmanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class InvalidInternshipPhaseException extends BusinessException {

  public InvalidInternshipPhaseException(String message) {
    super(HttpStatus.BAD_REQUEST, "INVALID_INTERNSHIP_PHASE", message);
  }
}
