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

  UserResponse getUser(Integer userId);

  UserResponse getCurrentUser(String username);

  UserResponse createUser(CreateUserRequest request);

  UserResponse updateUser(Integer userId, UpdateUserRequest request);

  UserResponse changeStatus(Integer userId, ChangeStatusRequest request);

  UserResponse changeRole(Integer userId, ChangeRoleRequest request, String actorUsername);

  void deleteUser(Integer userId);
}
