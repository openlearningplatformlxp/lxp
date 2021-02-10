package com.redhat.uxl.services.service.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.redhat.uxl.datalayer.dto.LearningPathProgressionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Learning path progression overview dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningPathProgressionOverviewDTO {

  private String programName;
  private Long programId;
  private Long userId;
  private BigDecimal courseCount;
  private BigDecimal coursesComplete;
  private BigDecimal percentComplete = BigDecimal.ZERO;
  private BigDecimal totalCourseDuration;
  private BigDecimal totalMinutesLeft;
  private Map<Long, BigDecimal> durationMap;
  private Date completedDate;
  private BigDecimal durationComplete;
  private List<LearningPathProgressionDTO> progressions;

    /**
     * Calculate completion.
     */
    public void calculateCompletion() {
    if (courseCount.intValue() > 0) {
      // Removing duration calculation and replacing with course based calculation
      percentComplete = coursesComplete.divide(courseCount, 2, BigDecimal.ROUND_HALF_UP);
    }
  }

    /**
     * Calculate time left.
     */
    public void calculateTimeLeft() {
    if (durationComplete != null && durationComplete.compareTo(BigDecimal.ZERO) > 0) {
      totalMinutesLeft =
          totalCourseDuration.subtract(durationComplete).multiply(new BigDecimal(60));
    } else {
      totalMinutesLeft = totalCourseDuration.multiply(new BigDecimal(60));
    }
  }
}
