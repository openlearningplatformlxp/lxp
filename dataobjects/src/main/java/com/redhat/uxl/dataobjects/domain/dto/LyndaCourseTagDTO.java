package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

/**
 * The type Lynda course tag dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LyndaCourseTagDTO implements Searchable {

  @JsonProperty("ID")
  private Long id;
  @JsonProperty("Name")
  private String name;
  @JsonProperty("TypeName")
  private String type;
}
