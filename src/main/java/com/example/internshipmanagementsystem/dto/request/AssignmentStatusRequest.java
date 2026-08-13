package com.example.internshipmanagementsystem.dto.request;

import com.example.internshipmanagementsystem.entity.AssignmentStatus;
import jakarta.validation.constraints.NotNull;

public record AssignmentStatusRequest(@NotNull AssignmentStatus status) {}
