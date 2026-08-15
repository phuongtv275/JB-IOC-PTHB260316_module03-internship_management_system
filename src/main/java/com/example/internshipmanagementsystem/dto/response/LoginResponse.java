package com.example.internshipmanagementsystem.dto.response;

public record LoginResponse(String accessToken, String tokenType, UserSummaryResponse user) {}
