package com.example.internshipmanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class InvalidAssessmentResultException extends BusinessException {

  public InvalidAssessmentResultException(String message) {
    super(HttpStatus.BAD_REQUEST, "INVALID_ASSESSMENT_RESULT", message);
  }
}
