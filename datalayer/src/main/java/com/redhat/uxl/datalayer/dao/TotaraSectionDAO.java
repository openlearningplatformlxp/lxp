package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO;

import java.util.List;
import java.util.Map;

/**
 * The interface Totara section dao.
 */
public interface TotaraSectionDAO {
    /**
     * Gets sections have activities for course.
     *
     * @param query the query
     * @return the sections have activities for course
     */
    List<Map<String, Object>> getSectionsHaveActivitiesForCourse(String query);

    /**
     * Gets section for course.
     *
     * @param courseId  the course id
     * @param sectionId the section id
     * @return the section for course
     */
    CoursePlayerSectionDTO getSectionForCourse(Long courseId, Long sectionId);

}
