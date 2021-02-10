package com.redhat.uxl.services.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

/**
 * The type Learning locker statement context dto.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LearningLockerStatementContextDTO implements Serializable {

  private LearningLockerStatementContextActivitiesDTO contextActivities;
}
