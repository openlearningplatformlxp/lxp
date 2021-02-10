package com.redhat.uxl.services.service.dto;

import com.redhat.uxl.dataobjects.domain.types.ProgramType;
import java.io.Serializable;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Achievement dto.
 */
@Data
public class AchievementDTO implements Serializable, Comparable<AchievementDTO> {

  private Long id;
  private String pictureUrl;
  private String title;
  private ProgramType type;
  private Integer progress;
  private DateTime completedDate;
  private DateTime timeEnrolled;
  private Integer status;

  @Override
  public int compareTo(AchievementDTO o) {
    if (o != null && o.completedDate != null) {
      // Sort by date - recent first
      return o.completedDate.compareTo(completedDate);
    }
    return -1;
  }

    /**
     * Gets sort date.
     *
     * @return the sort date
     */
    public DateTime getSortDate() {
    if (completedDate != null) {
      return completedDate;
    } else {
      return timeEnrolled;
    }
  }
}
