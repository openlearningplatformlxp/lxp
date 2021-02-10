package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;

/**
 * The type Totara team course dto.
 */
@Data
public class TotaraTeamCourseDTO {

  private Long userId;
  private Long course;
  private Integer status;
  private String firstName;
  private String lastName;
  private String shortName;
  private String fullName;
  private String description;
  private Long completedDate;
  private Long timeEnrolled;
  private String type;

}
