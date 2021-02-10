package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Course player activity quiz submit question dto.
 */
@Data
public class CoursePlayerActivityQuizSubmitQuestionDTO {
  private String type;
  private String slot;
  private String sequence_check;

  // multichoice
  private String answer_index;

  // multianswer
  private List<String> answer_indexes;

  // shortanswer
  private String answer_text;

  // truefalse
  private String answer_truefalse;

  // match
  private List<CoursePlayerActivityQuizSubmitQuestionMatchDTO> answer_match;

  // essay
  private String answer_essay;
}
