package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.datalayer.dto.CoursePlayerActivityStatusAvailabilityConstraintDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityStatusAvailabilityDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * The type Course player activity status dto.
 */
@Data
public class CoursePlayerActivityStatusDTO {
    private Long courseModuleId;
    private Integer status;
    private List<Map<String, Object>> requiredAction;
    private Boolean isLocked;
    private Boolean isRequired;
    private CoursePlayerActivityStatusAvailabilityConstraintDTO[] constraints;

    /**
     * Value of course player activity status dto.
     *
     * @param bo the bo
     * @return the course player activity status dto
     */
    public static CoursePlayerActivityStatusDTO valueOf(
            com.redhat.uxl.services.service.dto.CoursePlayerActivityStatusDTO bo) {
        CoursePlayerActivityStatusDTO dto = new CoursePlayerActivityStatusDTO();
        dto.setCourseModuleId(bo.getCoursemoduleid());
        dto.setStatus(bo.getCompletionstate());
        return dto;
    }

    /**
     * Value of list.
     *
     * @param bos the bos
     * @return the list
     */
    public static List<CoursePlayerActivityStatusDTO> valueOf(
            List<com.redhat.uxl.services.service.dto.CoursePlayerActivityStatusDTO> bos) {

        List<CoursePlayerActivityStatusDTO> activityDTOs = new ArrayList<>();
        for (com.redhat.uxl.services.service.dto.CoursePlayerActivityStatusDTO bo : bos) {
            activityDTOs.add(com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityStatusDTO.valueOf(bo));
        }

        return activityDTOs;
    }

    /**
     * Value of course player activity status dto.
     *
     * @param bo the bo
     * @return the course player activity status dto
     */
    public static CoursePlayerActivityStatusDTO valueOf(CoursePlayerActivityStatusAvailabilityDTO bo) {
        CoursePlayerActivityStatusDTO dto = new CoursePlayerActivityStatusDTO();
        dto.setCourseModuleId(bo.getId());
        if (bo.getCompletionStatus() != null) {
            dto.setStatus(bo.getCompletionStatus());
        } else {
            dto.setStatus(0);
        }
        dto.setIsLocked(bo.getIsLocked());
        dto.setRequiredAction(bo.getRequiredAction());
        dto.setConstraints(bo.getC());
        dto.setIsRequired(bo.isRequired());

        return dto;
    }

    /**
     * Value of available map.
     *
     * @param bos the bos
     * @return the map
     */
    public static Map<Long, CoursePlayerActivityStatusDTO> valueOfAvailable(
            List<CoursePlayerActivityStatusAvailabilityDTO> bos) {

        Map<Long, CoursePlayerActivityStatusDTO> activityDTOMap = new HashMap<>();

        for (CoursePlayerActivityStatusAvailabilityDTO bo : bos) {
            CoursePlayerActivityStatusDTO dto = com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityStatusDTO
                    .valueOf(bo);
            activityDTOMap.put(dto.getCourseModuleId(), dto);
        }

        return activityDTOMap;
    }
}
