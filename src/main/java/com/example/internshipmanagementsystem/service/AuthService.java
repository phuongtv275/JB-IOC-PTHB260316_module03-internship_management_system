package com.example.internshipmanagementsystem.service;

import com.example.internshipmanagementsystem.dto.request.LoginRequest;
import com.example.internshipmanagementsystem.dto.response.LoginResponse;

public interface AuthService {

  LoginResponse login(LoginRequest request);
}
