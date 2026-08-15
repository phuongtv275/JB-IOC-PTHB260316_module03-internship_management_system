package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.dto.response.UserSummaryResponse;
import com.example.internshipmanagementsystem.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserResponse toResponse(User user) {
    return new UserResponse(
        user.getUserId(),
        user.getUsername(),
        user.getFullName(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getRole(),
        user.isActive(),
        user.getCreatedAt(),
        user.getUpdatedAt());
  }

  public UserSummaryResponse toSummaryResponse(User user) {
    return new UserSummaryResponse(
        user.getUserId(), user.getUsername(), user.getFullName(), user.getRole(), user.isActive());
  }
}
