package com.redhat.uxl.dataobjects.domain.dto;

import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Totara program dto.
 */
@Data
public class TotaraProgramDTO implements Searchable {
  private Long userId;
  private Long courseId;
  private String programStatus;
  private String programName;
  private String programShortName;
  private String programSummary;
  private String courseFullName;
  private String courseShortName;
  private String courseSummary;
  private String courseStatus;
  private String moduleName;
  private String moduleStatus;
  private Long programId;
  private Long programTrackingId;
  private Long courseTrackingId;
  private Long moduleId;
  private Long moduleTrackingId;
  private Long dueDate;
  private Long completedDate;
  private Long timeCreated;
  private Float duration;
  private String hidesets;
  private Integer audienceVisible;
  private boolean enrolSelfEnabled;
  private String ceCredits;
  private Long timeEnrolled;
}
