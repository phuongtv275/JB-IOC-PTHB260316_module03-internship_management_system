package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.MentorProfileRequest;
import com.example.internshipmanagementsystem.dto.response.MentorResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.entity.Mentor;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.MentorMapper;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentorServiceImpl implements MentorService {

  private final MentorRepository mentorRepository;
  private final UserRepository userRepository;
  private final MentorMapper mentorMapper;

  @Override
  public PageResponse<MentorResponse> getMentors(Pageable pageable) {
    return PageResponse.from(mentorRepository.findAll(pageable).map(mentorMapper::toResponse));
  }

  @Override
  public MentorResponse getMentor(Integer mentorId, String actorUsername) {
    Mentor mentor = findMentor(mentorId);
    assertMentorOwnsProfile(mentor, actorUsername);
    return mentorMapper.toResponse(mentor);
  }

  @Override
  @Transactional
  public MentorResponse createMentor(MentorProfileRequest request) {
    User user = findUser(request.mentorId());
    requireRole(user, Role.MENTOR);
    if (mentorRepository.existsById(request.mentorId())) {
      throw new DuplicateResourceException("Mentor profile already exists");
    }
    return mentorMapper.toResponse(
        mentorRepository.save(Mentor.create(user, request.department(), request.academicRank())));
  }

  @Override
  @Transactional
  public MentorResponse updateMentor(
      Integer mentorId, MentorProfileRequest request, String actorUsername) {
    Mentor mentor = findMentor(mentorId);
    assertMentorOwnsProfile(mentor, actorUsername);
    mentor.update(request.department(), request.academicRank());
    return mentorMapper.toResponse(mentor);
  }

  private Mentor findMentor(Integer mentorId) {
    return mentorRepository
        .findById(mentorId)
        .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));
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

  private void assertMentorOwnsProfile(Mentor mentor, String actorUsername) {
    User actor = findUserByUsername(actorUsername);
    if (actor.getRole() == Role.MENTOR && !mentor.getUser().getUsername().equals(actorUsername)) {
      throw new AccessDeniedBusinessException("Mentors can only access their own profile");
    }
  }
}
