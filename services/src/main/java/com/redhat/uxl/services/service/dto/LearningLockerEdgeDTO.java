package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

/**
 * The type Learning locker edge dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerEdgeDTO implements Serializable {

  private String cursor;
  private LearningLockerNodeDTO node;
}
