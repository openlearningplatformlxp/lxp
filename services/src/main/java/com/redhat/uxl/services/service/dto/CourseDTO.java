package com.redhat.uxl.services.service.dto;

import java.util.List;
import lombok.Data;

/**
 * The type Course dto.
 */
@Data
public class CourseDTO {

  private Long courseId;
  private Long courseTrackingId;
  private String courseFullName;
  private String courseShortName;
  private String courseSummary;
  private String courseStatus;
  private String firstTopic;
  private List<ModuleDTO> modules;
  private boolean enrolled;

}
