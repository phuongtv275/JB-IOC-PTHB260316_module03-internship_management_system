package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.StudentProfileRequest;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.StudentDetailResponse;
import com.example.internshipmanagementsystem.dto.response.StudentSummaryResponse;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.Student;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.StudentMapper;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;
  private final UserRepository userRepository;
  private final StudentMapper studentMapper;

  @Override
  public PageResponse<StudentSummaryResponse> getStudents(String actorUsername, Pageable pageable) {
    User actor = findUserByUsername(actorUsername);
    if (actor.getRole() == Role.MENTOR) {
      return PageResponse.from(Page.empty(pageable));
    }
    return PageResponse.from(
        studentRepository.findAll(pageable).map(studentMapper::toSummaryResponse));
  }

  @Override
  public StudentDetailResponse getStudent(Integer studentId, String actorUsername) {
    Student student = findStudent(studentId);
    assertStudentOwnsProfile(student, actorUsername);
    return studentMapper.toDetailResponse(student);
  }

  @Override
  @Transactional
  public StudentDetailResponse createStudent(StudentProfileRequest request) {
    User user = findUser(request.studentId());
    requireRole(user, Role.STUDENT);
    if (studentRepository.existsById(request.studentId())
        || studentRepository.existsByStudentCode(request.studentCode())) {
      throw new DuplicateResourceException("Student profile or code already exists");
    }
    Student student =
        Student.create(
            user,
            request.studentCode(),
            request.major(),
            request.className(),
            request.dateOfBirth(),
            request.address());
    return studentMapper.toDetailResponse(studentRepository.save(student));
  }

  @Override
  @Transactional
  public StudentDetailResponse updateStudent(
      Integer studentId, StudentProfileRequest request, String actorUsername) {
    Student student = findStudent(studentId);
    assertStudentOwnsProfile(student, actorUsername);
    if (studentRepository.existsByStudentCodeAndStudentIdNot(request.studentCode(), studentId)) {
      throw new DuplicateResourceException("Student code already exists");
    }
    student.update(
        request.studentCode(),
        request.major(),
        request.className(),
        request.dateOfBirth(),
        request.address());
    return studentMapper.toDetailResponse(student);
  }

  private Student findStudent(Integer studentId) {
    return studentRepository
        .findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
  }

  private User findUser(Integer userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private User findUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private void requireRole(User user, Role role) {
    if (user.getRole() != role) {
      throw new AccessDeniedBusinessException("User does not have the required role");
    }
  }

  private void assertStudentOwnsProfile(Student student, String actorUsername) {
    User actor = findUserByUsername(actorUsername);
    if (actor.getRole() == Role.STUDENT && !student.getUser().getUsername().equals(actorUsername)) {
      throw new AccessDeniedBusinessException("Students can only access their own profile");
    }
  }
}
