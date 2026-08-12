package com.example.internshipmanagementsystem.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChangeStatusRequest(@NotNull Boolean isActive) {}
