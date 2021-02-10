package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import lombok.Data;

/**
 * The type Course player video time dto.
 */
@Data
public class CoursePlayerVideoTimeDTO {
    private Long courseId;
    private Long activityId;
    private Double videoTime;
}
