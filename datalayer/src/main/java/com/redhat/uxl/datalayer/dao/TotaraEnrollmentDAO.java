package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.datalayer.dto.EnrolledTotaraCourseDTO;
import com.redhat.uxl.datalayer.dto.TotaraServiceResponseDTO;

import java.util.List;
import java.util.Map;

/**
 * The interface Totara enrollment dao.
 */
public interface TotaraEnrollmentDAO {
    /**
     * Is user enrolled in course boolean.
     *
     * @param courseId       the course id
     * @param personTotaraId the person totara id
     * @return the boolean
     */
    Boolean isUserEnrolledInCourse(Long courseId, Long personTotaraId);

    /**
     * Gets enrolled courses by id.
     *
     * @param personTotaraId the person totara id
     * @param courseIds      the course ids
     * @return the enrolled courses by id
     */
    Map<Long, EnrolledTotaraCourseDTO> getEnrolledCoursesById(Long personTotaraId, List<Long> courseIds);

    /**
     * Enroll user to program totara service response dto.
     *
     * @param programId      the program id
     * @param personTotaraId the person totara id
     * @return the totara service response dto
     */
    TotaraServiceResponseDTO enrollUserToProgram(Long programId, Long personTotaraId);

    /**
     * Enroll user to course totara service response dto.
     *
     * @param courseid       the courseid
     * @param personTotaraId the person totara id
     * @return the totara service response dto
     */
    TotaraServiceResponseDTO enrollUserToCourse(Long courseid, Long personTotaraId);

    /**
     * Enroll user to program course totara service response dto.
     *
     * @param courseid       the courseid
     * @param personTotaraId the person totara id
     * @param programId      the program id
     * @return the totara service response dto
     */
    TotaraServiceResponseDTO enrollUserToProgramCourse(Long courseid, Long personTotaraId, Long programId);
}
