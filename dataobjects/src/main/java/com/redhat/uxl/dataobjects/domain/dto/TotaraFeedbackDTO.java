package com.redhat.uxl.dataobjects.domain.dto;

import lombok.Data;

/**
 * The type Totara feedback dto.
 */
@Data
public class TotaraFeedbackDTO {
  private Long id;
  private String email;
  private String region;
  private String learnerJobTitle;
}
