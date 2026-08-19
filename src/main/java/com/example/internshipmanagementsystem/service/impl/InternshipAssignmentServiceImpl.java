package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.InternshipAssignmentRequest;
import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentDetailResponse;
import com.example.internshipmanagementsystem.dto.response.InternshipAssignmentSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.entity.AssignmentStatus;
import com.example.internshipmanagementsystem.entity.InternshipAssignment;
import com.example.internshipmanagementsystem.entity.InternshipPhase;
import com.example.internshipmanagementsystem.entity.Mentor;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.Student;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.InternshipAssignmentMapper;
import com.example.internshipmanagementsystem.repository.InternshipAssignmentRepository;
import com.example.internshipmanagementsystem.repository.InternshipPhaseRepository;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.service.InternshipAssignmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class InternshipAssignmentServiceImpl implements InternshipAssignmentService {

  private final InternshipAssignmentRepository internshipAssignmentRepository;
  private final StudentRepository studentRepository;
  private final MentorRepository mentorRepository;
  private final InternshipPhaseRepository internshipPhaseRepository;
  private final UserRepository userRepository;
  private final InternshipAssignmentMapper internshipAssignmentMapper;

  @Override
  public PageResponse<InternshipAssignmentSummaryResponse> getAssignments(
      String actorUsername, Pageable pageable) {
    User actor = findUser(actorUsername);
    Page<InternshipAssignment> assignments =
        switch (actor.getRole()) {
          case ADMIN -> internshipAssignmentRepository.findAll(pageable);
          case MENTOR ->
              internshipAssignmentRepository.findByMentorUserUsername(actorUsername, pageable);
          case STUDENT ->
              internshipAssignmentRepository.findByStudentUserUsername(actorUsername, pageable);
        };
    return PageResponse.from(assignments.map(internshipAssignmentMapper::toSummaryResponse));
  }

  @Override
  public InternshipAssignmentDetailResponse getAssignment(
      Integer assignmentId, String actorUsername) {
    InternshipAssignment assignment = findAssignment(assignmentId);
    assertCanAccess(assignment, findUser(actorUsername));
    return internshipAssignmentMapper.toDetailResponse(assignment);
  }

  @Override
  @Transactional
  public InternshipAssignmentDetailResponse createAssignment(InternshipAssignmentRequest request) {
    if (internshipAssignmentRepository.existsByStudentStudentIdAndPhasePhaseId(
        request.studentId(), request.phaseId())) {
      throw new DuplicateResourceException("Student already has an assignment for this phase");
    }
    InternshipAssignment assignment =
        InternshipAssignment.create(
            findStudent(request.studentId()),
            findMentor(request.mentorId()),
            findPhase(request.phaseId()));
    InternshipAssignmentDetailResponse response =
        internshipAssignmentMapper.toDetailResponse(
            internshipAssignmentRepository.save(assignment));
    log.info(
        "IMS_EVENT ASSIGNMENT_CREATED ASSIGNMENT_ID={} STUDENT_ID={} MENTOR_ID={} PHASE_ID={}",
        response.assignmentId(),
        response.student().id(),
        response.mentor().id(),
        response.phase().id());
    return response;
  }

  @Override
  @Transactional
  public InternshipAssignmentDetailResponse updateStatus(
      Integer assignmentId, AssignmentStatus status) {
    InternshipAssignment assignment = findAssignment(assignmentId);
    assignment.changeStatus(status);
    return internshipAssignmentMapper.toDetailResponse(assignment);
  }

  private InternshipAssignment findAssignment(Integer assignmentId) {
    return internshipAssignmentRepository
        .findById(assignmentId)
        .orElseThrow(() -> new ResourceNotFoundException("Internship assignment not found"));
  }

  private Student findStudent(Integer studentId) {
    return studentRepository
        .findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
  }

  private Mentor findMentor(Integer mentorId) {
    return mentorRepository
        .findById(mentorId)
        .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));
  }

  private InternshipPhase findPhase(Integer phaseId) {
    return internshipPhaseRepository
        .findById(phaseId)
        .orElseThrow(() -> new ResourceNotFoundException("Internship phase not found"));
  }

  private User findUser(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private void assertCanAccess(InternshipAssignment assignment, User actor) {
    if (actor.getRole() == Role.MENTOR
        && !assignment.getMentor().getUser().getUserId().equals(actor.getUserId())) {
      throw new AccessDeniedBusinessException("Mentors can only access their assignments");
    }
    if (actor.getRole() == Role.STUDENT
        && !assignment.getStudent().getUser().getUserId().equals(actor.getUserId())) {
      throw new AccessDeniedBusinessException("Students can only access their assignments");
    }
  }
}
