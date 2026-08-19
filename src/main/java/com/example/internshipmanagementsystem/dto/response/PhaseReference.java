package com.example.internshipmanagementsystem.dto.response;

import java.time.LocalDate;

public record PhaseReference(Integer id, String name, LocalDate startDate, LocalDate endDate) {}
