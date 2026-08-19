package com.example.internshipmanagementsystem.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.entity.Mentor;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.Student;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class DefaultAccountInitializerTest {

  @Mock private UserRepository userRepository;
  @Mock private StudentRepository studentRepository;
  @Mock private MentorRepository mentorRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private DefaultAccountInitializer defaultAccountInitializer;

  @Test
  void shouldNotRegisterDefaultAccountInitializer_whenProductionProfileIsActive() {
    Profile profile = DefaultAccountInitializer.class.getAnnotation(Profile.class);

    assertThat(profile.value()).containsExactly("!prod");
  }

  @Test
  void shouldCreateMissingDefaultAccounts_whenApplicationStarts() throws Exception {
    when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
    when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    defaultAccountInitializer.run(new DefaultApplicationArguments());

    ArgumentCaptor<User> users = ArgumentCaptor.forClass(User.class);
    verify(userRepository, org.mockito.Mockito.times(3)).save(users.capture());

    assertThat(users.getAllValues())
        .extracting(User::getUsername, User::getRole)
        .containsExactlyInAnyOrder(
            org.assertj.core.groups.Tuple.tuple("ADMIN", Role.ADMIN),
            org.assertj.core.groups.Tuple.tuple("MENTOR", Role.MENTOR),
            org.assertj.core.groups.Tuple.tuple("STUDENT", Role.STUDENT));
  }

  @Test
  void shouldNotCreateDefaultAccounts_whenTheirUsernamesAlreadyExist() throws Exception {
    when(userRepository.findByUsername("ADMIN"))
        .thenReturn(Optional.of(createUser("ADMIN", Role.ADMIN)));
    when(userRepository.findByUsername("MENTOR"))
        .thenReturn(Optional.of(createUser("MENTOR", Role.MENTOR)));
    when(userRepository.findByUsername("STUDENT"))
        .thenReturn(Optional.of(createUser("STUDENT", Role.STUDENT)));
    when(mentorRepository.existsById(org.mockito.ArgumentMatchers.any())).thenReturn(true);
    when(studentRepository.existsById(org.mockito.ArgumentMatchers.any())).thenReturn(true);

    defaultAccountInitializer.run(new DefaultApplicationArguments());

    verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
    verify(passwordEncoder, never()).encode(anyString());
    verify(mentorRepository, never()).save(org.mockito.ArgumentMatchers.any());
    verify(studentRepository, never()).save(org.mockito.ArgumentMatchers.any());
  }

  @Test
  void shouldCreateProfiles_whenDefaultMentorAndStudentAccountsAreCreated() throws Exception {
    when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
    when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(mentorRepository.existsById(org.mockito.ArgumentMatchers.any())).thenReturn(false);
    when(studentRepository.existsById(org.mockito.ArgumentMatchers.any())).thenReturn(false);

    defaultAccountInitializer.run(new DefaultApplicationArguments());

    verify(mentorRepository).save(org.mockito.ArgumentMatchers.any(Mentor.class));
    verify(studentRepository).save(org.mockito.ArgumentMatchers.any(Student.class));
  }

  private User createUser(String username, Role role) {
    return User.create(username, "hash", username, username + "@example.com", null, role);
  }
}
