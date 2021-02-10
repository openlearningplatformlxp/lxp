package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * The type Learning locker response dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerResponseDTO implements Serializable {

  private List<LearningLockerEdgeDTO> edges;
  private LearningLockerPageInfoDTO pageInfo;
}
