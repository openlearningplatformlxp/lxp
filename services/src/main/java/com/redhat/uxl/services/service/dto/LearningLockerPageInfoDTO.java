package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

/**
 * The type Learning locker page info dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerPageInfoDTO implements Serializable {

  private Boolean hasNextPage;
  private Boolean hasPreviousPage;
  private String startCursor;
  private String endCursor;
}
