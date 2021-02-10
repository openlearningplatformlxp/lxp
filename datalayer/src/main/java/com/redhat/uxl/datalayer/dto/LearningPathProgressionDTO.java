package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * The type Learning path progression dto.
 */
@Data
public class LearningPathProgressionDTO {

  private String programName;
  private Long programId;
  private Long userId;
  private Long courseId;
  private Long status;
  private BigDecimal duration;
  private Long timecompleted;
  private Date timeCompletedFormatted;
  private Long cccompletion;

    /**
     * Is course complete boolean.
     *
     * @return the boolean
     */
    public boolean isCourseComplete() {
    return status != null && status > 49;
  }
}
