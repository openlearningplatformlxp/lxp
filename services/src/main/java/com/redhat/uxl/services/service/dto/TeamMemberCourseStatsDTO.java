package com.redhat.uxl.services.service.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Team member course stats dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberCourseStatsDTO {
  private String courseName;
  private BigDecimal percentComplete;
}
