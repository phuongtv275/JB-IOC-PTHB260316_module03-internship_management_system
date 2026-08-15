package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.MentorDetailResponse;
import com.example.internshipmanagementsystem.dto.response.MentorResponse;
import com.example.internshipmanagementsystem.dto.response.MentorSummaryResponse;
import com.example.internshipmanagementsystem.dto.response.UserResponse;
import com.example.internshipmanagementsystem.entity.Mentor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MentorMapper {

  private final UserMapper userMapper;

  public MentorResponse toResponse(Mentor mentor) {
    return new MentorResponse(
        mentor.getMentorId(),
        mentor.getDepartment(),
        mentor.getAcademicRank(),
        mentor.getCreatedAt(),
        mentor.getUpdatedAt());
  }

  public MentorSummaryResponse toSummaryResponse(Mentor mentor) {
    return new MentorSummaryResponse(
        mentor.getMentorId(),
        mentor.getUser().getFullName(),
        mentor.getDepartment(),
        mentor.getAcademicRank(),
        mentor.getUser().isActive());
  }

  public MentorDetailResponse toDetailResponse(Mentor mentor) {
    UserResponse account = userMapper.toResponse(mentor.getUser());
    return new MentorDetailResponse(
        mentor.getMentorId(),
        mentor.getDepartment(),
        mentor.getAcademicRank(),
        account,
        mentor.getCreatedAt(),
        mentor.getUpdatedAt());
  }
}
