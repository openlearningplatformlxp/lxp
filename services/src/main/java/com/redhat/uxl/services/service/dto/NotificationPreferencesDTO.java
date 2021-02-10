package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * The type Notification preferences dto.
 */
@Data
public class NotificationPreferencesDTO implements Serializable {

  private boolean newContentAvailable;
  private boolean beginLearningPath;
  private boolean beginCourse;
  private boolean completeLearningPath;
  private boolean completeCourse;
  private boolean overdueLearningPath;
  private boolean overdueCourse;
}
