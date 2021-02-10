package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import java.util.Map;
import lombok.Data;

/**
 * The type Course player activity quiz submit result dto.
 */
@Data
public class CoursePlayerActivityQuizSubmitResultDTO {
    private Map<String, Object> results;
    private CoursePlayerActivityStatusDTO activityStatus;
    private Map<Long, CoursePlayerActivityStatusDTO> activityStatuses;
}
