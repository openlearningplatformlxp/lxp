package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course player section dto.
 */
@Data
public class CoursePlayerSectionDTO {
    private Long id;
    private Long sortOrder;
    private String name;
    private List<com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityDTO> activities;

    /**
     * Value of course player section dto.
     *
     * @param bo the bo
     * @return the course player section dto
     */
    public static CoursePlayerSectionDTO valueOf(
        com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO bo) {
        CoursePlayerSectionDTO dto = new CoursePlayerSectionDTO();

        dto.setId(bo.getId());
        dto.setSortOrder(bo.getSortOrder());
        dto.setName(bo.getName());

        if (bo.getActivities() != null && !bo.getActivities().isEmpty()) {
            List<com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityDTO> activites = new ArrayList<>();
            for (CoursePlayerActivityDTO activityBO : bo.getActivities()) {
                activites.add(
                        com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerActivityDTO.valueOf(activityBO));
            }
            dto.setActivities(activites);
        }

        return dto;
    }
}
