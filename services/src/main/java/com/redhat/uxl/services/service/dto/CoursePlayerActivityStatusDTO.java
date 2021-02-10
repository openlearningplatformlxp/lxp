package com.redhat.uxl.services.service.dto;

import java.util.Map;
import lombok.Data;

/**
 * The type Course player activity status dto.
 */
@Data
public class CoursePlayerActivityStatusDTO {
    private Long coursemoduleid;
    private Integer completionstate;

    /**
     * Value of course player activity status dto.
     *
     * @param map the map
     * @return the course player activity status dto
     */
    public static CoursePlayerActivityStatusDTO valueOf(Map<String, Object> map) {
        CoursePlayerActivityStatusDTO bo = new CoursePlayerActivityStatusDTO();
        bo.setCoursemoduleid((Long) map.get("coursemoduleid"));
        bo.setCompletionstate((Integer) map.get("completionstate"));
        return bo;
    }
}
