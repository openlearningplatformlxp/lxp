package com.redhat.uxl.services.service;

import com.redhat.uxl.services.service.dto.CourseDTO;
import com.redhat.uxl.services.service.dto.CourseOverviewDTO;
import com.redhat.uxl.services.service.dto.CourseProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.CourseSectionsDTO;
import com.redhat.uxl.services.service.dto.LocationDTO;
import java.util.List;

/**
 * The interface Course service.
 */
public interface CourseService {

    /**
     * Gets course.
     *
     * @param personTotaraId the person totara id
     * @param courseId       the course id
     * @return the course
     */
    CourseDTO getCourse(Long personTotaraId, Long courseId);

    /**
     * Gets course overview.
     *
     * @param personTotaraId the person totara id
     * @param courseId       the course id
     * @return the course overview
     */
    CourseOverviewDTO getCourseOverview(Long personTotaraId, Long courseId);

    /**
     * Gets prerequisites course sections.
     *
     * @param courseId the course id
     * @return the prerequisites course sections
     */
    CourseSectionsDTO getPrerequisitesCourseSections(Long courseId);

    /**
     * Gets resources course sections.
     *
     * @param courseId the course id
     * @return the resources course sections
     */
    CourseSectionsDTO getResourcesCourseSections(Long courseId);

    /**
     * Gets general course sections.
     *
     * @param courseId       the course id
     * @param personTotaraId the person totara id
     * @return the general course sections
     */
    CourseSectionsDTO getGeneralCourseSections(Long courseId, Long personTotaraId);

    /**
     * Gets course progression overview.
     *
     * @param courseId       the course id
     * @param personTotaraId the person totara id
     * @return the course progression overview
     */
    CourseProgressionOverviewDTO getCourseProgressionOverview(Long courseId, Long personTotaraId);

    /**
     * Gets event locations.
     *
     * @return the event locations
     */
    List<LocationDTO> getEventLocations();
}
