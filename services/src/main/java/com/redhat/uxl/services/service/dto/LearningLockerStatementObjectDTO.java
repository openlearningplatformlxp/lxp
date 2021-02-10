package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

/**
 * The type Learning locker statement object dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerStatementObjectDTO implements Serializable {

  private String objectType;
  private String id;
  private LearningLockerStatementObjectDefinitionDTO definition;

}
