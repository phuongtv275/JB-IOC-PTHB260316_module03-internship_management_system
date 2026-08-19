package com.example.internshipmanagementsystem.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.dto.request.ChangeRoleRequest;
import com.example.internshipmanagementsystem.dto.request.CreateUserRequest;
import com.example.internshipmanagementsystem.dto.request.UpdateUserRequest;
import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.mapper.UserMapper;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

class UserServiceImplTest {

  private final UserRepository userRepository = Mockito.mock(UserRepository.class);
  private final StudentRepository studentRepository = Mockito.mock(StudentRepository.class);
  private final MentorRepository mentorRepository = Mockito.mock(MentorRepository.class);
  private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
  private final UserServiceImpl userService =
      new UserServiceImpl(
          userRepository, studentRepository, mentorRepository, passwordEncoder, new UserMapper());

  @Test
  void shouldRequestDatabasePage_whenUsersAreRetrieved() {
    User user =
        User.create("student", "hash", "Student", "student@example.com", null, Role.STUDENT);
    PageRequest pageable = PageRequest.of(1, 5);
    when(userRepository.findByRole(Role.STUDENT, pageable))
        .thenReturn(new PageImpl<>(List.of(user), pageable, 6));

    var response = userService.getUsers(Role.STUDENT, pageable);

    org.assertj.core.api.Assertions.assertThat(response.content()).hasSize(1);
    org.assertj.core.api.Assertions.assertThat(response.page()).isEqualTo(1);
    org.assertj.core.api.Assertions.assertThat(response.totalElements()).isEqualTo(6);
    verify(userRepository).findByRole(Role.STUDENT, pageable);
  }

  @Test
  void shouldCreateUser_whenUsernameAndEmailAreAvailable() {
    CreateUserRequest request =
        new CreateUserRequest(
            "student", "password", "Student Name", "student@example.com", "", Role.STUDENT);
    when(userRepository.existsByUsername("student")).thenReturn(false);
    when(userRepository.existsByEmail("student@example.com")).thenReturn(false);
    when(passwordEncoder.encode("password")).thenReturn("hash");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    UserResponse response = userService.createUser(request);

    org.assertj.core.api.Assertions.assertThat(response.username()).isEqualTo("student");
    org.assertj.core.api.Assertions.assertThat(response.role()).isEqualTo(Role.STUDENT);
  }

  @Test
  void shouldThrowException_whenUsernameAlreadyExists() {
    CreateUserRequest request =
        new CreateUserRequest(
            "student", "password", "Student Name", "student@example.com", null, Role.STUDENT);
    when(userRepository.existsByUsername("student")).thenReturn(true);

    assertThatThrownBy(() -> userService.createUser(request))
        .isInstanceOf(DuplicateResourceException.class);
  }

  @Test
  void shouldThrowException_whenAdminChangesAnotherAdminRole() {
    User actor = User.create("admin", "hash", "Admin", "admin@example.com", null, Role.ADMIN);
    User target =
        User.create("other-admin", "hash", "Other Admin", "other@example.com", null, Role.ADMIN);
    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(actor));
    when(userRepository.findById(2)).thenReturn(Optional.of(target));

    assertThatThrownBy(
            () -> userService.changeRole(2, new ChangeRoleRequest(Role.STUDENT), "admin"))
        .isInstanceOf(AccessDeniedBusinessException.class);
  }

  @Test
  void shouldKeepRole_whenUpdatingBasicUserInformation() {
    User user = User.create("admin", "hash", "Admin", "admin@example.com", null, Role.ADMIN);
    UpdateUserRequest request =
        new UpdateUserRequest(
            "admin", "new-password", "Updated Admin", "updated@example.com", "0123456789");
    when(userRepository.findById(1)).thenReturn(Optional.of(user));
    when(userRepository.existsByUsernameAndUserIdNot("admin", 1)).thenReturn(false);
    when(userRepository.existsByEmailAndUserIdNot("updated@example.com", 1)).thenReturn(false);
    when(passwordEncoder.encode("new-password")).thenReturn("new-hash");

    UserResponse response = userService.updateUser(1, request);

    org.assertj.core.api.Assertions.assertThat(response.role()).isEqualTo(Role.ADMIN);
  }

  @Test
  void shouldThrowException_whenStudentWithProfileChangesToAnotherRole() {
    User student =
        User.create("student", "hash", "Student", "student@example.com", null, Role.STUDENT);
    ReflectionTestUtils.setField(student, "userId", 1);
    when(userRepository.findById(1)).thenReturn(Optional.of(student));
    when(studentRepository.existsById(1)).thenReturn(true);

    assertThatThrownBy(() -> userService.changeRole(1, new ChangeRoleRequest(Role.MENTOR), "admin"))
        .isInstanceOf(AccessDeniedBusinessException.class);
  }

  @Test
  void shouldThrowException_whenMentorWithProfileChangesToAnotherRole() {
    User mentor = User.create("mentor", "hash", "Mentor", "mentor@example.com", null, Role.MENTOR);
    ReflectionTestUtils.setField(mentor, "userId", 1);
    when(userRepository.findById(1)).thenReturn(Optional.of(mentor));
    when(mentorRepository.existsById(1)).thenReturn(true);

    assertThatThrownBy(
            () -> userService.changeRole(1, new ChangeRoleRequest(Role.STUDENT), "admin"))
        .isInstanceOf(AccessDeniedBusinessException.class);
  }
}
