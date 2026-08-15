package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.ChangeRoleRequest;
import com.example.internshipmanagementsystem.dto.request.ChangeStatusRequest;
import com.example.internshipmanagementsystem.dto.request.CreateUserRequest;
import com.example.internshipmanagementsystem.dto.request.UpdateUserRequest;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.dto.response.UserSummaryResponse;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.UserMapper;
import com.example.internshipmanagementsystem.repository.MentorRepository;
import com.example.internshipmanagementsystem.repository.StudentRepository;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final StudentRepository studentRepository;
  private final MentorRepository mentorRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  @Override
  public PageResponse<UserSummaryResponse> getUsers(Role role, Pageable pageable) {
    Page<User> users =
        role == null ? userRepository.findAll(pageable) : userRepository.findByRole(role, pageable);
    return PageResponse.from(users.map(userMapper::toSummaryResponse));
  }

  @Override
  public UserResponse getUser(Integer userId) {
    return userMapper.toResponse(findUser(userId));
  }

  @Override
  public UserResponse getCurrentUser(String username) {
    return userMapper.toResponse(findUserByUsername(username));
  }

  @Override
  @Transactional
  public UserResponse createUser(CreateUserRequest request) {
    validateNewIdentity(request.username(), request.email());
    User user =
        User.create(
            request.username(),
            passwordEncoder.encode(request.password()),
            request.fullName(),
            request.email(),
            request.phoneNumber(),
            request.role());
    UserResponse response = userMapper.toResponse(userRepository.save(user));
    log.info("IMS_EVENT USER_CREATED USER_ID={} ROLE={}", response.userId(), response.role());
    return response;
  }

  @Override
  @Transactional
  public UserResponse updateUser(Integer userId, UpdateUserRequest request) {
    User user = findUser(userId);
    validateUpdatedIdentity(userId, request.username(), request.email());
    user.update(
        request.username(),
        passwordEncoder.encode(request.password()),
        request.fullName(),
        request.email(),
        request.phoneNumber());
    return userMapper.toResponse(user);
  }

  @Override
  @Transactional
  public UserResponse changeStatus(Integer userId, ChangeStatusRequest request) {
    User user = findUser(userId);
    user.changeActiveStatus(request.isActive());
    return userMapper.toResponse(user);
  }

  @Override
  @Transactional
  public UserResponse changeRole(Integer userId, ChangeRoleRequest request, String actorUsername) {
    User target = findUser(userId);
    if (target.getRole() == Role.ADMIN && !target.getUsername().equals(actorUsername)) {
      throw new AccessDeniedBusinessException(
          "An administrator cannot change another administrator's role");
    }
    validateProfileRoleChange(target, request.role());
    target.changeRole(request.role());
    return userMapper.toResponse(target);
  }

  @Override
  @Transactional
  public void deleteUser(Integer userId) {
    userRepository.delete(findUser(userId));
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

  private void validateNewIdentity(String username, String email) {
    if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
      throw new DuplicateResourceException("Username or email already exists");
    }
  }

  private void validateUpdatedIdentity(Integer userId, String username, String email) {
    if (userRepository.existsByUsernameAndUserIdNot(username, userId)
        || userRepository.existsByEmailAndUserIdNot(email, userId)) {
      throw new DuplicateResourceException("Username or email already exists");
    }
  }

  private void validateProfileRoleChange(User user, Role newRole) {
    if (user.getRole() == Role.STUDENT
        && newRole != Role.STUDENT
        && studentRepository.existsById(user.getUserId())) {
      throw new AccessDeniedBusinessException(
          "A user with a student profile must keep the STUDENT role");
    }
    if (user.getRole() == Role.MENTOR
        && newRole != Role.MENTOR
        && mentorRepository.existsById(user.getUserId())) {
      throw new AccessDeniedBusinessException(
          "A user with a mentor profile must keep the MENTOR role");
    }
  }
}
