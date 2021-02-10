package com.redhat.uxl.datalayer.dto;

import lombok.Data;

/**
 * The type Course player activity content quiz content question matching stem dto.
 */
@Data
public class CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO {
  private String value;
  private String choiceIndex; // pre-selected value from in-progress attempts.
}
