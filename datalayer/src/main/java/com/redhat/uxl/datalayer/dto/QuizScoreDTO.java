package com.redhat.uxl.datalayer.dto;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * The type Quiz score dto.
 */
@Data
public class QuizScoreDTO {
  private boolean success;
  private boolean passed;
  private String message;
  private BigDecimal attempt_grade;
  private DateTime timestart;
  private DateTime timefinish;
  private Long attempt_number;
  private Long allowed_attempts;
  private Long number_correct;
  private Long moduleid;
  private Long activityid;
  private BigDecimal total_questions;
  private Boolean course_complete;
  private QuizScoreFeedbackDTO[] feedback;
}
