package com.example.internshipmanagementsystem.dto.request;

import com.example.internshipmanagementsystem.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank @Size(max = 50) String username,
    @NotBlank @Size(min = 8, max = 255) String password,
    @NotBlank @Size(max = 100) String fullName,
    @NotBlank @Email @Size(max = 100) String email,
    @Size(max = 20) String phoneNumber,
    @NotNull Role role) {}
