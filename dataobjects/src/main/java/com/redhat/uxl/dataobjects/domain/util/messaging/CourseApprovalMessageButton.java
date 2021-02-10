package com.redhat.uxl.dataobjects.domain.util.messaging;

import lombok.Data;

/**
 * The type Course approval message button.
 */
@Data
public class CourseApprovalMessageButton extends MessageButton {
  private Long personId;
  private Long sessionId;
  private Long courseId;
}
