package com.example.internshipmanagementsystem.dto.response;

import com.example.internshipmanagementsystem.entity.AssignmentStatus;
import java.time.LocalDateTime;

public record InternshipAssignmentResponse(
    Integer assignmentId,
    Integer studentId,
    Integer mentorId,
    Integer phaseId,
    LocalDateTime assignedDate,
    AssignmentStatus status) {}
