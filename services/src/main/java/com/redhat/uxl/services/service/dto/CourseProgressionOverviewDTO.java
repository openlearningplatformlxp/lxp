package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

/**
 * The type Course progression overview dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseProgressionOverviewDTO {

  private String courseName;
  private Long activityCount;
  private Long activityCompleteCount;
  private int percentComplete;
  private boolean hideProgressEvents;
  private DateTime completionDate;
  private ProgramType type;

    /**
     * The Progress events.
     */
    List<CourseProgressEventDTO> progressEvents;
}
