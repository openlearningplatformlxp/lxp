package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Personal program stats dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalProgramStatsDTO implements Serializable {
  private List<PersonalProgramStatIndividualDTO> individuals = new ArrayList<>();
  private List<TeamMemberProgressOverviewDTO> progressOverview = new ArrayList<>();
}
