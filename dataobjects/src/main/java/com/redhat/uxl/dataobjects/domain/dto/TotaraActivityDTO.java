package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;

/**
 * The type Totara activity dto.
 */
@Data
public class TotaraActivityDTO {
  private String name;
  private Long status;
  private Long id;
  private Long courseId;
}
