package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Course player activity content quiz content question dto.
 */
@Data
public class CoursePlayerActivityContentQuizContentQuestionDTO implements Comparable {
  private Long questionid;
  private String question_text;
  private Long slot;
  private String answer_order;
  private String sequence_check;
  private String current_user_answer;
  private String generalfeedback;
  private String type;
  private List<CoursePlayerActivityContentQuizContentQuestionMatchingStemDTO> match_stems;
  private List<CoursePlayerActivityContentQuizContentQuestionMatchingChoiceDTO> match_choices;
  private List<CoursePlayerActivityContentQuizContentQuestionAnswerDTO> answers;

  public int compareTo(Object o) {
    CoursePlayerActivityContentQuizContentQuestionDTO a =
        (CoursePlayerActivityContentQuizContentQuestionDTO) o;
    if (this.getSlot() > a.getSlot()) {
      return 1;
    } else if (this.getSlot() < a.getSlot()) {
      return -1;
    }
    return 0;
  }
}
