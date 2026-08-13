package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.MentorResponse;
import com.example.internshipmanagementsystem.entity.Mentor;
import org.springframework.stereotype.Component;

@Component
public class MentorMapper {

  public MentorResponse toResponse(Mentor mentor) {
    return new MentorResponse(
        mentor.getMentorId(),
        mentor.getDepartment(),
        mentor.getAcademicRank(),
        mentor.getCreatedAt(),
        mentor.getUpdatedAt());
  }
}
