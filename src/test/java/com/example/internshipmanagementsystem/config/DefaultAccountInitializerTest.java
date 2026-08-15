package com.example.internshipmanagementsystem.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.repository.UserRepository;
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
  @Mock private PasswordEncoder passwordEncoder;
  @InjectMocks private DefaultAccountInitializer defaultAccountInitializer;

  @Test
  void shouldNotRegisterDefaultAccountInitializer_whenProductionProfileIsActive() {
    Profile profile = DefaultAccountInitializer.class.getAnnotation(Profile.class);

    assertThat(profile.value()).containsExactly("!prod");
  }

  @Test
  void shouldCreateMissingDefaultAccounts_whenApplicationStarts() throws Exception {
    when(userRepository.existsByUsername(anyString())).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> invocation.getArgument(0));

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
    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    defaultAccountInitializer.run(new DefaultApplicationArguments());

    verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
    verify(passwordEncoder, never()).encode(anyString());
  }
}
