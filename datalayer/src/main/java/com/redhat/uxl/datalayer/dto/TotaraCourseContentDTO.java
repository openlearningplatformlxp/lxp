package com.redhat.uxl.datalayer.dto;

import com.redhat.uxl.dataobjects.domain.types.ProgramCourseType;
import lombok.Data;

/**
 * The type Totara course content dto.
 */
@Data
public class TotaraCourseContentDTO {

  private Long courseID;

  private String courseFullName;

  private String description;

  private ProgramCourseType courseType;

  private Long activityId;

}
