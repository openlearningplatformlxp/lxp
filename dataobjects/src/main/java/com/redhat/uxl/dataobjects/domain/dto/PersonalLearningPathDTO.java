package com.redhat.uxl.dataobjects.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.redhat.uxl.dataobjects.domain.PersonalPlanShare;
import lombok.Data;
import org.joda.time.DateTime;

/**
 * The type Personal learning path dto.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalLearningPathDTO {
  private Long id;
  private String title;
  private String description;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
  private Date dueDate;
  private boolean archived;
  private boolean sharedWithManager;
  private DateTime sharedWithManagerOn;
  private boolean personal = true;
  private long userId;
  private String manager;
  private List<PersonalPlanShare> shares;
  private BigDecimal percentComplete;
  private BigDecimal totalCourses;
  private BigDecimal totalCompletedCourses;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMddyyyy")
  private Date dueDateUpdated;

  private List<ProgramCourseSetDTO> courseSets = null;

    /**
     * Is shared boolean.
     *
     * @return the boolean
     */
    public boolean isShared() {
    return sharedWithManager || isSharedWithReports();
  }

    /**
     * Is shared with reports boolean.
     *
     * @return the boolean
     */
    public boolean isSharedWithReports() {
    return shares != null && !shares.isEmpty();
  }

}
