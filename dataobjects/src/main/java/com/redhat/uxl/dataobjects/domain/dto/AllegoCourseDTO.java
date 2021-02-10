package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redhat.uxl.dataobjects.domain.Searchable;
import lombok.Data;

import java.util.Date;

/**
 * The type Allego course dto.
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AllegoCourseDTO implements Searchable {

  private static final String LEVEL = "Level";
  private static final String TOPIC = "Topic";
  @JsonProperty("contentId")
  private Long id;
  @JsonProperty("name")
  private String name;
  @JsonProperty("description")
  private String description;
  @JsonProperty("contentPurpose")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy hh:mm:ss a")
  private Date timeCreated;

}
