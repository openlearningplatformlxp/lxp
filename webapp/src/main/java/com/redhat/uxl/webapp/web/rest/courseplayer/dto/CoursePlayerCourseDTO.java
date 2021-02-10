package com.redhat.uxl.webapp.web.rest.courseplayer.dto;

import com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * The type Course player course dto.
 */
@Data
public class CoursePlayerCourseDTO {

    private Long id;
    private String fullName;
    private String shortName;
    private List<com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerSectionDTO> sections;

    /**
     * Value of course player course dto.
     *
     * @param bo the bo
     * @return the course player course dto
     */
    public static CoursePlayerCourseDTO valueOf(
        com.redhat.uxl.datalayer.dto.CoursePlayerCourseDTO bo) {
        CoursePlayerCourseDTO dto = new CoursePlayerCourseDTO();

        dto.setId(bo.getId());
        dto.setFullName(bo.getFullName());
        dto.setShortName(bo.getShortName());

        List<com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerSectionDTO> sections = new ArrayList<>();
        for (CoursePlayerSectionDTO sectionBO : bo.getSections()) {
            sections.add(com.redhat.uxl.webapp.web.rest.courseplayer.dto.CoursePlayerSectionDTO.valueOf(sectionBO));
        }

        dto.setSections(sections);
        return dto;
    }

}
