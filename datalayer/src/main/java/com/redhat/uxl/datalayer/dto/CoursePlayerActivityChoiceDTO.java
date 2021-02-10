package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Course player activity choice dto.
 */
@Data
public class CoursePlayerActivityChoiceDTO {
  private String name;
  private String intro;
  private Integer numberofanswers;
  private Boolean allowmultiple;
  private List<CoursePlayerActivityChoiceOptionDTO> options;
  private String message;
  private Boolean success;
}
