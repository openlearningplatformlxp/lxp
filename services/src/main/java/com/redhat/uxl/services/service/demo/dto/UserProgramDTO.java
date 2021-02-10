package com.redhat.uxl.services.service.demo.dto;

import lombok.Data;

/**
 * The type User program dto.
 */
@Data
public class UserProgramDTO {
  private Long userId;
  private Long programId;
  private Boolean complete = false;
  private Long enrollmentDate;
  private String status;
}
