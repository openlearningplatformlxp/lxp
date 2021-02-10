package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Linkedin course urls dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedinCourseUrlsDTO implements Searchable {

  private String webLaunch;
  private String ssoLaunch;
}
