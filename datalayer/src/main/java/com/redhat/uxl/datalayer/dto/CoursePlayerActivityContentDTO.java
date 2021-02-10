package com.redhat.uxl.datalayer.dto;

import com.redhat.uxl.dataobjects.domain.ScormValue;
import lombok.Data;

import java.util.List;

/**
 * The type Course player activity content dto.
 */
@Data
public class CoursePlayerActivityContentDTO {

  // resource/url/scorm
  private String url;
  private Boolean shouldDisplayInNewWindow;
  private Double videoTime;

  // feedback
  private List<CoursePlayerActivityContentFeedbackItemDTO> items;

  // scorm
  private List<ScormValue> values;

  // quiz
  private CoursePlayerActivityContentQuizDTO quiz;

  // label/page
  private String html;

  // page
  private String description;

}
