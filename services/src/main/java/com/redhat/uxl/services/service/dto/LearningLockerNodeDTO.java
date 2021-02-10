package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * The type Learning locker node dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerNodeDTO implements Serializable {

  @JsonProperty("_id")
  private String id;

  private LearningLockerStatementDTO statement;
}
