package com.redhat.uxl.datalayer.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The type Totara program dto.
 */
@Data
public class TotaraProgramDTO {
    private Long id;
    private Long category;
    private String fullName;
    private String shortName;
    private Long certificationId;
    private Boolean isUserEnrolled;
    private Boolean isSelfEnrollEnabled = false;

    private List<TotaraCourseSetDTO> courseSets;
    private Map<Long, EnrolledTotaraCourseDTO> enrolledCourses;

    /**
     * Value of totara program dto.
     *
     * @param map the map
     * @return the totara program dto
     */
    public static TotaraProgramDTO valueOf(Map<String, Object> map) {
        TotaraProgramDTO bo = new TotaraProgramDTO();
        bo.setId((Long) map.get("id"));
        bo.setCategory((Long) map.get("category"));
        bo.setFullName((String) map.get("fullname"));
        bo.setShortName((String) map.get("shortname"));
        bo.setCertificationId((Long) map.get("certifid"));

        return bo;
    }
}
