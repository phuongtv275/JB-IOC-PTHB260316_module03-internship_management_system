package com.example.internshipmanagementsystem.mapper;

import com.example.internshipmanagementsystem.dto.response.CriterionReference;
import com.example.internshipmanagementsystem.dto.response.RoundCriterionDetailResponse;
import com.example.internshipmanagementsystem.dto.response.RoundCriterionResponse;
import com.example.internshipmanagementsystem.dto.response.RoundReference;
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

  public RoundCriterionDetailResponse toDetailResponse(RoundCriterion criterion) {
    return new RoundCriterionDetailResponse(
        criterion.getRoundCriterionId(),
        new RoundReference(criterion.getRound().getRoundId(), criterion.getRound().getRoundName()),
        new CriterionReference(
            criterion.getCriterion().getCriterionId(), criterion.getCriterion().getCriterionName(),
            criterion.getCriterion().getDescription(), criterion.getCriterion().getMaxScore()),
        criterion.getWeight());
  }
}
