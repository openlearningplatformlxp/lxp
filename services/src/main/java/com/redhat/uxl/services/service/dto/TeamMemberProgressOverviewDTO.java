package com.redhat.uxl.services.service.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Team member progress overview dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberProgressOverviewDTO {
  private ProgramItemDTO program;
  private BigDecimal percentComplete;
  private List<TeamMemberCompletionDTO> teamMemberCompletionList;

    /**
     * Calculate percent.
     */
    public void calculatePercent() {
    BigDecimal total = BigDecimal.ZERO;
    for (TeamMemberCompletionDTO dto : getTeamMemberCompletionList()) {
      if (dto.getPercentComplete() != null) {
        total = total.add(dto.getPercentComplete());
      }
    }
    setPercentComplete(
        total.divide(new BigDecimal(getTeamMemberCompletionList().size()), RoundingMode.HALF_UP));
  }
}
