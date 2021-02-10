package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

/**
 * The type Learning locker statement dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerStatementDTO implements Serializable {

  private String id;
  private String version;
  private String timestamp;
  private LearningLockerStatementContextDTO context;
  private LearningLockerStatementActorDTO actor;
  private LearningLockerStatementObjectDTO object;
  private LearningLockerStatementVerbDTO verb;
}
