package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import java.util.Map;
import lombok.Data;

/**
 * The type Scorm value set result dto.
 */
@Data
public class ScormValueSetResultDTO {
    private Boolean isCompleted;
    private CoursePlayerActivityStatusDTO activityStatus;
    private Map<Long, CoursePlayerActivityStatusDTO> activityStatuses;
}
