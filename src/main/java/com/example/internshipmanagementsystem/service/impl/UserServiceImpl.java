package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.ChangeRoleRequest;
import com.example.internshipmanagementsystem.dto.request.ChangeStatusRequest;
import com.example.internshipmanagementsystem.dto.request.CreateUserRequest;
import com.example.internshipmanagementsystem.dto.request.UpdateUserRequest;
import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import com.example.internshipmanagementsystem.exception.AccessDeniedBusinessException;
import com.example.internshipmanagementsystem.exception.DuplicateResourceException;
import com.example.internshipmanagementsystem.exception.ResourceNotFoundException;
import com.example.internshipmanagementsystem.mapper.UserMapper;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  @Override
  public List<UserResponse> getUsers(Role role) {
    List<User> users = role == null ? userRepository.findAll() : userRepository.findByRole(role);
    return users.stream().map(userMapper::toResponse).toList();
  }

  @Override
  public UserResponse getUser(Long userId) {
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
    return userMapper.toResponse(userRepository.save(user));
  }

  @Override
  @Transactional
  public UserResponse updateUser(Long userId, UpdateUserRequest request) {
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
  public UserResponse changeStatus(Long userId, ChangeStatusRequest request) {
    User user = findUser(userId);
    user.changeActiveStatus(request.isActive());
    return userMapper.toResponse(user);
  }

  @Override
  @Transactional
  public UserResponse changeRole(Long userId, ChangeRoleRequest request, String actorUsername) {
    User target = findUser(userId);
    if (target.getRole() == Role.ADMIN && !target.getUsername().equals(actorUsername)) {
      throw new AccessDeniedBusinessException(
          "An administrator cannot change another administrator's role");
    }
    target.changeRole(request.role());
    return userMapper.toResponse(target);
  }

  @Override
  @Transactional
  public void deleteUser(Long userId) {
    userRepository.delete(findUser(userId));
  }

  private User findUser(Long userId) {
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

  private void validateUpdatedIdentity(Long userId, String username, String email) {
    if (userRepository.existsByUsernameAndUserIdNot(username, userId)
        || userRepository.existsByEmailAndUserIdNot(email, userId)) {
      throw new DuplicateResourceException("Username or email already exists");
    }
  }
}
