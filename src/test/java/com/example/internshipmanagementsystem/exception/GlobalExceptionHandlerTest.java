package com.example.internshipmanagementsystem.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.internshipmanagementsystem.dto.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

  @Test
  void shouldReturnConflict_whenDatabaseConstraintIsViolated() {
    ResponseEntity<ErrorResponse> response =
        exceptionHandler.handleDataIntegrityViolation(
            new DataIntegrityViolationException("related records exist"));

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody().errorCode()).isEqualTo("RESOURCE_CONFLICT");
  }
}
