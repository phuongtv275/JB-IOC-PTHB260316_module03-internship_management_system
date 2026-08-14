package com.example.internshipmanagementsystem.controller;

import com.example.internshipmanagementsystem.dto.request.ChangeRoleRequest;
import com.example.internshipmanagementsystem.dto.request.ChangeStatusRequest;
import com.example.internshipmanagementsystem.dto.request.CreateUserRequest;
import com.example.internshipmanagementsystem.dto.request.UpdateUserRequest;
import com.example.internshipmanagementsystem.dto.response.ApiResponse;
import com.example.internshipmanagementsystem.dto.response.PageResponse;
import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getUsers(
      @RequestParam(required = false) Role role, @PageableDefault(size = 5) Pageable pageable) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200, "Users retrieved", PageResponse.from(userService.getUsers(role), pageable)));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Integer userId) {
    return ResponseEntity.ok(
        ApiResponse.success(200, "User retrieved", userService.getUser(userId)));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<UserResponse>> createUser(
      @Valid @RequestBody CreateUserRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success(201, "User created", userService.createUser(request)));
  }

  @PutMapping("/{userId}")
  public ResponseEntity<ApiResponse<UserResponse>> updateUser(
      @PathVariable Integer userId, @Valid @RequestBody UpdateUserRequest request) {
    return ResponseEntity.ok(
        ApiResponse.success(200, "User updated", userService.updateUser(userId, request)));
  }

  @PutMapping("/{userId}/status")
  public ResponseEntity<ApiResponse<UserResponse>> changeStatus(
      @PathVariable Integer userId, @Valid @RequestBody ChangeStatusRequest request) {
    return ResponseEntity.ok(
        ApiResponse.success(200, "User status updated", userService.changeStatus(userId, request)));
  }

  @PutMapping("/{userId}/role")
  public ResponseEntity<ApiResponse<UserResponse>> changeRole(
      @PathVariable Integer userId,
      @Valid @RequestBody ChangeRoleRequest request,
      @AuthenticationPrincipal UserDetails userDetails) {
    return ResponseEntity.ok(
        ApiResponse.success(
            200,
            "User role updated",
            userService.changeRole(userId, request, userDetails.getUsername())));
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }
}
