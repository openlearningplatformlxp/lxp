package com.redhat.uxl.services.service.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.uxl.datalayer.dto.CoursePlayerActivityStatusAvailabilityDTO;
import lombok.Data;

/**
 * The type Course activity status dto.
 */
@Data
public class CourseActivityStatusDTO {
    private Long courseModuleId;
    private Integer status;
    private Boolean isLocked;
    private Long timecompleted;

    /**
     * Value of course activity status dto.
     *
     * @param bo the bo
     * @return the course activity status dto
     */
    public static CourseActivityStatusDTO valueOf(CoursePlayerActivityStatusAvailabilityDTO bo) {
        CourseActivityStatusDTO dto = new CourseActivityStatusDTO();
        dto.setCourseModuleId(bo.getId());
        dto.setTimecompleted(bo.getCompletionDate());
        if (bo.getCompletionStatus() != null) {
            dto.setStatus(bo.getCompletionStatus());
        } else {
            dto.setStatus(0);
        }
        dto.setIsLocked(bo.getIsLocked());

        return dto;
    }

    /**
     * Value of available map.
     *
     * @param bos the bos
     * @return the map
     */
    public static Map<Long, CourseActivityStatusDTO> valueOfAvailable(
            List<CoursePlayerActivityStatusAvailabilityDTO> bos) {

        Map<Long, CourseActivityStatusDTO> activityDTOMap = new HashMap<>();

        for (CoursePlayerActivityStatusAvailabilityDTO bo : bos) {
            CourseActivityStatusDTO dto = CourseActivityStatusDTO.valueOf(bo);
            activityDTOMap.put(dto.getCourseModuleId(), dto);
        }

        return activityDTOMap;
    }
}
