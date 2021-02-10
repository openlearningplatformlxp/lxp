package com.redhat.uxl.services.service.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Course set progression overview dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSetProgressionOverviewDTO {

  private String courseSetName;
  private List<CourseProgressionOverviewDTO> progressionOverviews;
}
