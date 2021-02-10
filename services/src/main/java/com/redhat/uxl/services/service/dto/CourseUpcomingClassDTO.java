package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Course upcoming class dto.
 */
@Data
public class CourseUpcomingClassDTO implements Serializable {

  private Long sessionId;
  private Long faceToFaceId;
  private DateTime startTime;
  private DateTime endTime;
  private String city;
  private String state;
  private String country;
  private BigDecimal price;
  private boolean verified;
  private boolean soldOut;
  private boolean enrolled;
  private boolean requiresManagerApproval;
  private boolean requestPending;

}
