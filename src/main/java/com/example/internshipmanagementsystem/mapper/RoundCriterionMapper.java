package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.RoundCriterionResponse;
import com.example.internshipmanagementsystem.entity.RoundCriterion;
import org.springframework.stereotype.Component;

@Component
public class RoundCriterionMapper {

  public RoundCriterionResponse toResponse(RoundCriterion criterion) {
    return new RoundCriterionResponse(
        criterion.getRoundCriterionId(),
        criterion.getRound().getRoundId(),
        criterion.getCriterion().getCriterionId(),
        criterion.getWeight());
  }
}
