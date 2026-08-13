package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record InternshipPhaseRequest(
    @NotBlank @Size(max = 100) String phaseName,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    String description) {}
