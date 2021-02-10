package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * The type Quiz score feedback dto.
 */
@Data
public class QuizScoreFeedbackDTO {
  private String feedbacktext;
  private BigDecimal mingrade;
  private BigDecimal maxgrade;
}
