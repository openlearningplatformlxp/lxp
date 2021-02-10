package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Linkedin course details dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedinCourseDetailsDTO implements Searchable {

  private String level;
  private LinkedinCourseStringDTO descriptionIncludingHtml;
  private LinkedinCourseStringDTO description;
  private LinkedinCourseStringDTO shortDescription;
  private LinkedinCourseStringDTO shortDescriptionIncludingHtml;
  private LinkedinCourseUrlsDTO urls;
  private LinkedinCourseTimeDTO timeToComplete;
}
