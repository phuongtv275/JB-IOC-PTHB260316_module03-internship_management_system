package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.ChangeRoleRequest;
import com.example.internshipmanagementsystem.dto.request.ChangeStatusRequest;
import com.example.internshipmanagementsystem.dto.request.CreateUserRequest;
import com.example.internshipmanagementsystem.dto.request.UpdateUserRequest;
import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.entity.Role;
import java.util.List;

public interface UserService {

  List<UserResponse> getUsers(Role role);

  UserResponse getUser(Long userId);

  UserResponse getCurrentUser(String username);

  UserResponse createUser(CreateUserRequest request);

  UserResponse updateUser(Long userId, UpdateUserRequest request);

  UserResponse changeStatus(Long userId, ChangeStatusRequest request);

  UserResponse changeRole(Long userId, ChangeRoleRequest request, String actorUsername);

  void deleteUser(Long userId);
}
