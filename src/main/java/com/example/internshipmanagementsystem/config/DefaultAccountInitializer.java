package com.example.internshipmanagementsystem.config;

import com.example.internshipmanagementsystem.entity.Mentor;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.Student;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!prod")
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
  private static final String DEFAULT_STUDENT_CODE = "DEFAULT-STUDENT";
  private static final String DEFAULT_MENTOR_DEPARTMENT = "Internship Management";
  private static final String DEFAULT_MENTOR_ACADEMIC_RANK = "Mentor";

  private final UserRepository userRepository;
  private final StudentRepository studentRepository;
  private final MentorRepository mentorRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    createIfMissing(ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL, Role.ADMIN);
    User mentor = createIfMissing(MENTOR_USERNAME, MENTOR_PASSWORD, MENTOR_EMAIL, Role.MENTOR);
    User student = createIfMissing(STUDENT_USERNAME, STUDENT_PASSWORD, STUDENT_EMAIL, Role.STUDENT);
    createMentorProfileIfMissing(mentor);
    createStudentProfileIfMissing(student);
  }

  private User createIfMissing(String username, String password, String email, Role role) {
    return userRepository
        .findByUsername(username)
        .orElseGet(
            () -> {
              User user =
                  userRepository.save(
                      User.create(
                          username,
                          passwordEncoder.encode(password),
                          username,
                          email,
                          DEFAULT_PHONE_NUMBER,
                          role));
              log.info("IMS_EVENT DEFAULT_ACCOUNT_CREATED ROLE={}", role);
              return user;
            });
  }

  private void createMentorProfileIfMissing(User user) {
    if (user.getRole() == Role.MENTOR && !mentorRepository.existsById(user.getUserId())) {
      mentorRepository.save(
          Mentor.create(user, DEFAULT_MENTOR_DEPARTMENT, DEFAULT_MENTOR_ACADEMIC_RANK));
    }
  }

  private void createStudentProfileIfMissing(User user) {
    if (user.getRole() == Role.STUDENT && !studentRepository.existsById(user.getUserId())) {
      studentRepository.save(Student.create(user, DEFAULT_STUDENT_CODE, null, null, null, null));
    }
  }
}
