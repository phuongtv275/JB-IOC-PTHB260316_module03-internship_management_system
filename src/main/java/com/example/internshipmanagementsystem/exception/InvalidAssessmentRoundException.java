package com.example.internshipmanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class InvalidAssessmentRoundException extends BusinessException {

  public InvalidAssessmentRoundException(String message) {
    super(HttpStatus.BAD_REQUEST, "INVALID_ASSESSMENT_ROUND", message);
  }
}
