package com.redhat.uxl.webapp.web.rest.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * The type Course text entry dto.
 */
@Data
public class CourseTextEntryDTO implements Serializable {

    private Long textEntryId;
    private Long activityId;
    private String message;
}
