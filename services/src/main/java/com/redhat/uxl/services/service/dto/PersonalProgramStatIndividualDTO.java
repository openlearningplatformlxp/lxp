package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Personal program stat individual dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalProgramStatIndividualDTO
    implements Serializable, Comparable<PersonalProgramStatIndividualDTO> {
  private String name;
  private String avatar;
  private Integer plans;
  private Integer sharedWithYou;

  @Override
  public int compareTo(PersonalProgramStatIndividualDTO o) {
    return plans.compareTo(o.plans);
  }
}
