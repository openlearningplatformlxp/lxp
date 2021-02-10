package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.dataobjects.domain.types.ActivityType;
import java.util.Map;
import lombok.Data;

/**
 * The type Course player activity dto.
 */
@Data
public class CoursePlayerActivityDTO {
    private Long id;
    private Long courseId;
    private String name;
    private ActivityType type;
    private Long status;
    private CoursePlayerActivityStatusDTO activityStatus;
    private String originalActivityType;

    private Boolean allowsManualCompletion;
    private Boolean shouldCompleteOnView;

    private CoursePlayerActivityContentDTO content;

    private Map<Long, CoursePlayerActivityStatusDTO> activityStatuses;

    private String externalURL;

    /**
     * Value of course player activity dto.
     *
     * @param bo the bo
     * @return the course player activity dto
     */
    public static CoursePlayerActivityDTO valueOf(
        com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO bo) {
        CoursePlayerActivityDTO dto = new CoursePlayerActivityDTO();
        dto.setId(bo.getId());
        dto.setName(bo.getName());
        dto.setType(bo.getType());
        dto.setStatus(bo.getStatus());
        dto.setAllowsManualCompletion(bo.getAllowsManualCompletion());
        dto.setShouldCompleteOnView(bo.getShouldCompleteOnView());
        dto.setOriginalActivityType(bo.getOriginalActivityType());
        return dto;
    }

    /**
     * Value of with content course player activity dto.
     *
     * @param bo the bo
     * @return the course player activity dto
     */
    public static CoursePlayerActivityDTO valueOfWithContent(
            com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO bo) {
        CoursePlayerActivityDTO dto = valueOf(bo);

        dto.setContent(CoursePlayerActivityContentDTO.valueOf(bo.getContent()));

        return dto;
    }
}
