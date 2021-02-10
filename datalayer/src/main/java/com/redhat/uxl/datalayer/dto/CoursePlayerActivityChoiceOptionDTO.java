package com.redhat.uxl.datalayer.dto;

import lombok.Data;

/**
 * The type Course player activity choice option dto.
 */
@Data
public class CoursePlayerActivityChoiceOptionDTO {
  private Long optionid;
  private String optiontext;
  private Long choiceid;
  private Boolean selected;
  private Integer number;
}
