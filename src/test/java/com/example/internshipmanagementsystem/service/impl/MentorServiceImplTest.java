package com.example.internshipmanagementsystem.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.dto.request.MentorProfileRequest;
import com.example.internshipmanagementsystem.entity.Mentor;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.mapper.MentorMapper;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MentorServiceImplTest {

  private final MentorRepository mentorRepository = Mockito.mock(MentorRepository.class);
  private final UserRepository userRepository = Mockito.mock(UserRepository.class);
  private final MentorServiceImpl mentorService =
      new MentorServiceImpl(mentorRepository, userRepository, new MentorMapper());

  @Test
  void shouldThrowException_whenMentorReadsAnotherProfile() {
    User actor = User.create("mentor", "hash", "Mentor", "mentor@example.com", null, Role.MENTOR);
    User target = User.create("other", "hash", "Other", "other@example.com", null, Role.MENTOR);
    Mentor mentor = Mentor.create(target, null, null);
    when(userRepository.findByUsername("mentor")).thenReturn(Optional.of(actor));
    when(mentorRepository.findById(2L)).thenReturn(Optional.of(mentor));

    assertThatThrownBy(() -> mentorService.getMentor(2L, "mentor"))
        .isInstanceOf(AccessDeniedBusinessException.class);
  }

  @Test
  void shouldThrowException_whenProfileUserDoesNotHaveMentorRole() {
    User user =
        User.create("student", "hash", "Student", "student@example.com", null, Role.STUDENT);
    MentorProfileRequest request = new MentorProfileRequest(1L, null, null);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    assertThatThrownBy(() -> mentorService.createMentor(request))
        .isInstanceOf(AccessDeniedBusinessException.class);
  }
}
