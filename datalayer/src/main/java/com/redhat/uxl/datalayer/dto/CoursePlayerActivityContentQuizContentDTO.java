package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Course player activity content quiz content dto.
 */
@Data
public class CoursePlayerActivityContentQuizContentDTO {
  private Long quizattemptid;
  private String state;
  private String description;
  private String question_order;
  private Boolean shuffle_questions;
  private Boolean shuffle_answers;
  private String sumgrades;
  private String grade;
  private String grademethod;
  private String preferredbehaviour;
  private Boolean filteringoption;
  private Long passing_score;
  private Boolean areAttemptsRunOut;
  private List<CoursePlayerActivityContentQuizContentQuestionDTO> questions;
}
