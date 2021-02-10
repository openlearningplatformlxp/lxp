package com.redhat.uxl.services.service.dto;

import lombok.Data;

/**
 * The type Totara course dto.
 */
@Data
public class TotaraCourseDTO {

    /**
     * The Id.
     */
    Long id;
    /**
     * The Full name.
     */
    String fullName;
    /**
     * The Short name.
     */
    String shortName;
    /**
     * The Summary.
     */
    String summary;

    /**
     * Convert totara course totara course dto.
     *
     * @param course the course
     * @return the totara course dto
     */
    public static TotaraCourseDTO convertTotaraCourse(
        com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO course) {
        TotaraCourseDTO dto = new TotaraCourseDTO();
        dto.setFullName(course.getFullName());
        dto.setShortName(course.getShortName());
        dto.setSummary(course.getSummary());
        dto.setId(course.getId());
        return dto;
    }
}
