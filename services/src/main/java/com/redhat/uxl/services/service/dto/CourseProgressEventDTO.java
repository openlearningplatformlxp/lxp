package com.redhat.uxl.services.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

/**
 * The type Course progress event dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseProgressEventDTO {

  private String status;
  private String categoryName;
  private String description;
  private DateTime date;
}
