package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.util.Map;

/**
 * The type Course player activity content quiz dto.
 */
@Data

public class CoursePlayerActivityContentQuizDTO {

  private Long moduleid;
  private Long courseid;
  private String name;
  private String type;
  private Long sortorder;
  private Boolean required;
  private String availabilityopperator;
  private Map<String, Object>[] availabilityrequirements;
  private CoursePlayerActivityContentQuizContentDTO content;

}
