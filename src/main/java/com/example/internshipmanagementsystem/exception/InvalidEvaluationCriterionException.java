package com.example.internshipmanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class InvalidEvaluationCriterionException extends BusinessException {

  public InvalidEvaluationCriterionException(String message) {
    super(HttpStatus.BAD_REQUEST, "INVALID_EVALUATION_CRITERION", message);
  }
}
