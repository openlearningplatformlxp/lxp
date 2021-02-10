package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;

/**
 * The type Totara message dto.
 */
@Data
public class TotaraMessageDTO {
  private Long id;
  private String title;
  private String message;
  private Long userId;
  private Long action;
  private Long session;
}
