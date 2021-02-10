package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * The type Learning locker statement context activities dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerStatementContextActivitiesDTO implements Serializable {

  private List<LearningLockerStatementObjectDTO> parent;
}
