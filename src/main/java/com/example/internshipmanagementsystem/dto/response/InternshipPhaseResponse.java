package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDate;

public record InternshipPhaseResponse(
    Long phaseId, String phaseName, LocalDate startDate, LocalDate endDate, String description) {}
