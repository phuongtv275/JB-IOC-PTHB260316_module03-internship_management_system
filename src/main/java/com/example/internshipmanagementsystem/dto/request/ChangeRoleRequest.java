package com.example.internshipmanagementsystem.dto.request;

import com.example.internshipmanagementsystem.entity.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(@NotNull Role role) {}
