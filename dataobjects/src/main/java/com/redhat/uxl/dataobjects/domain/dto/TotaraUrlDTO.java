package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;

/**
 * The type Totara url dto.
 */
@Data
public class TotaraUrlDTO {
  private Long id;
  private Long course;
  private String name;
  private String externalUrl;
}
