package com.example.internshipmanagementsystem.dto.response;

import com.example.internshipmanagementsystem.entity.AssignmentStatus;
import java.time.LocalDateTime;

public record InternshipAssignmentResponse(
    Long assignmentId,
    Long studentId,
    Long mentorId,
    Long phaseId,
    LocalDateTime assignedDate,
    AssignmentStatus status) {}
