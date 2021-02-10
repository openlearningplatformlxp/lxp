package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Linkedin course dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedinCourseDTO implements Searchable {

  private String urn;
  private LinkedinCourseDetailsDTO details;
  private LinkedinCourseStringDTO title;
}
