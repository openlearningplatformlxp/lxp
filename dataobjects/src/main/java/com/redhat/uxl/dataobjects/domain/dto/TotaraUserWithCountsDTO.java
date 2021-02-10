package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;

/**
 * The type Totara user with counts dto.
 */
@Data
public class TotaraUserWithCountsDTO extends TotaraUserDTO {
  private int programCount;
  private int courseCount;
  private int activityCount;
}
