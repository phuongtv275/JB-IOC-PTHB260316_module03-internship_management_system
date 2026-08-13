package com.example.internshipmanagementsystem.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.dto.request.StudentProfileRequest;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.Student;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.mapper.StudentMapper;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class StudentServiceImplTest {

  private final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
  private final UserRepository userRepository = Mockito.mock(UserRepository.class);
  private final StudentServiceImpl studentService =
      new StudentServiceImpl(studentRepository, userRepository, new StudentMapper());

  @Test
  void shouldThrowException_whenStudentUpdatesAnotherProfile() {
    User actor =
        User.create("student", "hash", "Student", "student@example.com", null, Role.STUDENT);
    User target = User.create("other", "hash", "Other", "other@example.com", null, Role.STUDENT);
    Student student = Student.create(target, "SV002", null, null, null, null);
    StudentProfileRequest request = new StudentProfileRequest(2L, "SV002", null, null, null, null);
    when(userRepository.findByUsername("student")).thenReturn(Optional.of(actor));
    when(studentRepository.findById(2L)).thenReturn(Optional.of(student));

    assertThatThrownBy(() -> studentService.updateStudent(2L, request, "student"))
        .isInstanceOf(AccessDeniedBusinessException.class);
  }

  @Test
  void shouldThrowException_whenProfileUserDoesNotHaveStudentRole() {
    User user = User.create("mentor", "hash", "Mentor", "mentor@example.com", null, Role.MENTOR);
    StudentProfileRequest request = new StudentProfileRequest(1L, "SV001", null, null, null, null);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    assertThatThrownBy(() -> studentService.createStudent(request))
        .isInstanceOf(AccessDeniedBusinessException.class);
  }
}
