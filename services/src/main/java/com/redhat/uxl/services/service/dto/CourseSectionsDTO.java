package com.redhat.uxl.services.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO;
import lombok.Data;

/**
 * The type Course sections dto.
 */
@Data
public class CourseSectionsDTO implements Serializable {

  private List<CoursePlayerSectionDTO> sections = new ArrayList<>();

  private Map<Long, CourseActivityStatusDTO> activityStatuses;

  private CourseProgressDTO courseProgress;
}
