package com.redhat.uxl.datalayer.dto;

import com.redhat.uxl.dataobjects.domain.types.ActivityType;
import lombok.Data;

/**
 * The type Course player activity dto.
 */
@Data
public class CoursePlayerActivityDTO {
  private Long id;
  private Long courseId;
  private String name;
  private ActivityType type;
  private Long status;
  private Long timecompleted;
  private Boolean isLocked;
  private CoursePlayerActivityStatusAvailabilityDTO completionStatus;
  private String originalActivityType;

  private Boolean allowsManualCompletion;
  private Boolean shouldCompleteOnView;

  private CoursePlayerActivityContentDTO content;
}
