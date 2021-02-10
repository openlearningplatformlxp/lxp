package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The type Course player course dto.
 */
@Data
public class CoursePlayerCourseDTO {
    private Long id;
    private String fullName;
    private String shortName;
    private List<CoursePlayerSectionDTO> sections;

    /**
     * Instantiates a new Course player course dto.
     *
     * @param map the map
     */
    public CoursePlayerCourseDTO(Map<String, Object> map) {
        this.setId((Long) map.get("id"));
        this.setFullName((String) map.get("fullName"));
        this.setShortName((String) map.get("shortName"));
    }
}
