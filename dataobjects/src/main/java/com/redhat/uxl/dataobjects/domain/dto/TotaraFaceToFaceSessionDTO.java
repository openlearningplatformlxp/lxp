package com.redhat.uxl.dataobjects.domain.dto;

import java.math.BigDecimal;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Totara face to face session dto.
 */
@Data
public class TotaraFaceToFaceSessionDTO {
  private Long sessionId;
  private Long faceToFaceId;
  private Long approvalType; // 4 requires approval
  private Long capacity;
  private BigDecimal price;
  private Long roomId;
  private String timezone;
  private DateTime startDate;
  private DateTime endDate;
  private String courseName;
  private Long courseId;
}
