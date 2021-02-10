package com.redhat.uxl.datalayer.dto;

import lombok.Data;

/**
 * The type Course player activity content quiz content question answer dto.
 */
@Data
public class CoursePlayerActivityContentQuizContentQuestionAnswerDTO {
  private Long answerid;
  private String answer_text;
  private String feedback;
  private Long fraction;
}
