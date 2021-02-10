package com.redhat.uxl.services.service.demo.dto;

import lombok.Data;

/**
 * The type User course dto.
 */
@Data
public class UserCourseDTO {
  private Long userId;
  private Long courseId;
  private Boolean complete = false;
  private Boolean locked = false;
}
