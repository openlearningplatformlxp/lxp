package com.redhat.uxl.services.service.dto;

import java.util.List;
import lombok.Data;

/**
 * The type Learning path dto.
 */
@Data
public class LearningPathDTO {
  private Long programId;
  private Long programTrackingId;
  private String programName;
  private String programStatus;
  private String programSummary;
  private List<CourseDTO> courses;

}
