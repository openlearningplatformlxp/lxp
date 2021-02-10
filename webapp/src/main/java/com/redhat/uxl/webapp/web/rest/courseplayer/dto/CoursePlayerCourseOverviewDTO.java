package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import java.util.Map;
import lombok.Data;

/**
 * The type Course player course overview dto.
 */
@Data
public class CoursePlayerCourseOverviewDTO {

    private CoursePlayerCourseDTO courseData;

    private Map<Long, CoursePlayerActivityStatusDTO> activityStatuses;
}
