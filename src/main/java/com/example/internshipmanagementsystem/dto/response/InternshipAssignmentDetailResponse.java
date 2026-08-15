package com.example.internshipmanagementsystem.dto.response;

import com.example.internshipmanagementsystem.entity.AssignmentStatus;
import java.time.LocalDateTime;

public record InternshipAssignmentDetailResponse(
    Integer assignmentId,
    AssignmentStatus status,
    LocalDateTime assignedAt,
    StudentReference student,
    MentorReference mentor,
    PhaseReference phase) {}
