package com.redhat.uxl.services.service;

import com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO;
import java.util.List;

/**
 * The interface Totara section service.
 */
public interface TotaraSectionService {

    /**
     * Gets resource sections have activities for course.
     *
     * @param courseId the course id
     * @return the resource sections have activities for course
     */
    List<CoursePlayerSectionDTO> getResourceSectionsHaveActivitiesForCourse(Long courseId);

    /**
     * Gets prerequisites sections have activities for course.
     *
     * @param courseId the course id
     * @return the prerequisites sections have activities for course
     */
    List<CoursePlayerSectionDTO> getPrerequisitesSectionsHaveActivitiesForCourse(Long courseId);

    /**
     * Gets general sections have activities for course.
     *
     * @param courseId the course id
     * @return the general sections have activities for course
     */
    List<CoursePlayerSectionDTO> getGeneralSectionsHaveActivitiesForCourse(Long courseId);

    /**
     * Gets section for course.
     *
     * @param courseId  the course id
     * @param sectionId the section id
     * @return the section for course
     */
    CoursePlayerSectionDTO getSectionForCourse(Long courseId, Long sectionId);

}
