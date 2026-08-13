package com.example.internshipmanagementsystem.config;

import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultAccountInitializer implements ApplicationRunner {

  private static final String ADMIN_USERNAME = "ADMIN";
  private static final String MENTOR_USERNAME = "MENTOR";
  private static final String STUDENT_USERNAME = "STUDENT";
  private static final String ADMIN_PASSWORD = "admin";
  private static final String MENTOR_PASSWORD = "mentor";
  private static final String STUDENT_PASSWORD = "student";
  private static final String DEFAULT_PHONE_NUMBER = "";
  private static final String ADMIN_EMAIL = "admin@internship.local";
  private static final String MENTOR_EMAIL = "mentor@internship.local";
  private static final String STUDENT_EMAIL = "student@internship.local";

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    createIfMissing(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL, Role.ADMIN);
    createIfMissing(MENTOR_USERNAME, MENTOR_PASSWORD, MENTOR_EMAIL, Role.MENTOR);
    createIfMissing(STUDENT_USERNAME, STUDENT_PASSWORD, STUDENT_EMAIL, Role.STUDENT);
  }

  private void createIfMissing(String username, String password, String email, Role role) {
    if (userRepository.existsByUsername(username)) {
      return;
    }

    userRepository.save(
        User.create(
            username,
            passwordEncoder.encode(password),
            username,
            email,
            DEFAULT_PHONE_NUMBER,
            role));
    log.info("Created default {} account", role);
  }
}
