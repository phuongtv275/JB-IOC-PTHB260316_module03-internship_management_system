package com.example.internshipmanagementsystem.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.internshipmanagementsystem.entity.Mentor;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.Student;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileAuthorizationIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private UserRepository userRepository;
  @Autowired private StudentRepository studentRepository;
  @Autowired private MentorRepository mentorRepository;

  @MockitoBean private JwtService jwtService;

  private User student;
  private User otherStudent;
  private User mentor;
  private User otherMentor;

  @BeforeEach
  void setUp() {
    studentRepository.deleteAll();
    mentorRepository.deleteAll();
    userRepository.deleteAll();
    when(jwtService.extractUsername(anyString()))
        .thenAnswer(invocation -> invocation.getArgument(0));

    student = saveUser("student", Role.STUDENT);
    otherStudent = saveUser("other-student", Role.STUDENT);
    mentor = saveUser("mentor", Role.MENTOR);
    otherMentor = saveUser("other-mentor", Role.MENTOR);
    studentRepository.save(Student.create(student, "SV001", null, null, null, null));
    studentRepository.save(Student.create(otherStudent, "SV002", null, null, null, null));
    mentorRepository.save(Mentor.create(mentor, null, null));
    mentorRepository.save(Mentor.create(otherMentor, null, null));
  }

  @Test
  void shouldRejectStudentProfileCreation_whenActorIsNotAdmin() throws Exception {
    mockMvc
        .perform(
            post("/api/students")
                .header(HttpHeaders.AUTHORIZATION, "Bearer student")
                .contentType("application/json")
                .content("{\"studentId\":1,\"studentCode\":\"SV003\"}"))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectMentorProfileCreation_whenActorIsNotAdmin() throws Exception {
    mockMvc
        .perform(
            post("/api/mentors")
                .header(HttpHeaders.AUTHORIZATION, "Bearer student")
                .contentType("application/json")
                .content("{\"mentorId\":1}"))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectStudentAccessAndUpdate_whenProfileBelongsToAnotherStudent() throws Exception {
    mockMvc
        .perform(
            get("/api/students/{studentId}", otherStudent.getUserId())
                .header("Authorization", "Bearer student"))
        .andExpect(status().isForbidden());
    mockMvc
        .perform(
            put("/api/students/{studentId}", otherStudent.getUserId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer student")
                .contentType("application/json")
                .content("{\"studentId\":2,\"studentCode\":\"SV002\"}"))
        .andExpect(status().isForbidden());
  }

  @Test
  void shouldRejectMentorAccessAndUpdate_whenProfileBelongsToAnotherMentor() throws Exception {
    mockMvc
        .perform(
            get("/api/mentors/{mentorId}", otherMentor.getUserId())
                .header("Authorization", "Bearer mentor"))
        .andExpect(status().isForbidden());
    mockMvc
        .perform(
            put("/api/mentors/{mentorId}", otherMentor.getUserId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer mentor")
                .contentType("application/json")
                .content("{\"mentorId\":2}"))
        .andExpect(status().isForbidden());
  }

  private User saveUser(String username, Role role) {
    return userRepository.save(
        User.create(username, "hash", username, username + "@example.com", null, role));
  }
}
