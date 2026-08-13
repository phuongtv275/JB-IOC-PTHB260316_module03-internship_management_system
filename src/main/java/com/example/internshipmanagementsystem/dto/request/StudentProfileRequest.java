package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record StudentProfileRequest(
    @NotNull Long studentId,
    @NotBlank @Size(max = 20) String studentCode,
    @Size(max = 100) String major,
    @Size(max = 50) String className,
    LocalDate dateOfBirth,
    @Size(max = 255) String address) {}
