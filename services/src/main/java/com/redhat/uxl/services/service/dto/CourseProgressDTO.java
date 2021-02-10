package com.redhat.uxl.services.service.dto;

import lombok.Data;

/**
 * The type Course progress dto.
 */
@Data
public class CourseProgressDTO {

  private int activityCount;
  private int activityCompleteCount;
  private int percentComplete;

}
