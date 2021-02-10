package com.redhat.uxl.datalayer.dto;

import lombok.Data;

/**
 * The type Course player activity content feedback item dto.
 */
@Data
public class CoursePlayerActivityContentFeedbackItemDTO {
  private Long id;
  private String label;
  private String name;
  private String presentation;
  private String type;
  private Integer position;
  private Integer required;
}
