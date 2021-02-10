package com.redhat.uxl.datalayer.dto;

import lombok.Data;

/**
 * The type Course player activity status availability constraint dto.
 */
@Data
public class CoursePlayerActivityStatusAvailabilityConstraintDTO {
  private String type;
  private Boolean showC;

  // completion
  private Long cm;
  private Long e;

  // grade
  private Long max;
  private Long min;
  private Long id;
}
