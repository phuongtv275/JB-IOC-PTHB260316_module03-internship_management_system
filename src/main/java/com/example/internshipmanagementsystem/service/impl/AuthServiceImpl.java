package com.example.internshipmanagementsystem.service.impl;

import com.example.internshipmanagementsystem.dto.request.LoginRequest;
import com.example.internshipmanagementsystem.dto.response.LoginResponse;
import com.example.internshipmanagementsystem.mapper.UserMapper;
import com.example.internshipmanagementsystem.repository.UserRepository;
import com.example.internshipmanagementsystem.security.JwtService;
import com.example.internshipmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private static final String BEARER = "Bearer";

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public LoginResponse login(LoginRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                request.username(), request.password()));
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return new LoginResponse(
        jwtService.generateToken(userDetails),
        BEARER,
        userRepository
            .findByUsername(userDetails.getUsername())
            .map(userMapper::toSummaryResponse)
            .orElseThrow());
  }
}
