package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDate;

public record InternshipPhaseResponse(
    Integer phaseId,
    String phaseName,
    LocalDate startDate,
    LocalDate endDate,
    String description) {}
