package com.redhat.uxl.services.service;

import com.redhat.uxl.datalayer.dto.CoursePlayerActivityChoiceDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityQuizSubmitDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityStatusAvailabilityDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerCourseDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerSectionDTO;
import com.redhat.uxl.datalayer.dto.TotaraActivityCompletionDTO;
import java.util.List;
import java.util.Map;

/**
 * The interface Course player service.
 */
public interface CoursePlayerService {

    /**
     * Gets course and sections have acitivites for user.
     *
     * @param courseId          the course id
     * @param personTotaraId    the person totara id
     * @param includeActivities the include activities
     * @return the course and sections have acitivites for user
     */
    CoursePlayerCourseDTO getCourseAndSectionsHaveAcitivitesForUser(Long courseId, Long personTotaraId,
            boolean includeActivities);

    /**
     * Gets activities for section for course.
     *
     * @param courseId       the course id
     * @param sectionId      the section id
     * @param personTotaraId the person totara id
     * @return the activities for section for course
     */
    CoursePlayerSectionDTO getActivitiesForSectionForCourse(Long courseId, Long sectionId, Long personTotaraId);

    /**
     * Gets activity content.
     *
     * @param courseId       the course id
     * @param activityId     the activity id
     * @param personId       the person id
     * @param personTotaraId the person totara id
     * @return the activity content
     */
    CoursePlayerActivityDTO getActivityContent(Long courseId, Long activityId, Long personId, Long personTotaraId);

    /**
     * Mark activity complete course player activity dto.
     *
     * @param courseId       the course id
     * @param activityId     the activity id
     * @param personId       the person id
     * @param personTotaraId the person totara id
     * @return the course player activity dto
     */
    CoursePlayerActivityDTO markActivityComplete(Long courseId, Long activityId, Long personId, Long personTotaraId);

    /**
     * Mark activity incomplete course player activity dto.
     *
     * @param courseId       the course id
     * @param activityId     the activity id
     * @param personId       the person id
     * @param personTotaraId the person totara id
     * @return the course player activity dto
     */
    CoursePlayerActivityDTO markActivityIncomplete(Long courseId, Long activityId, Long personId, Long personTotaraId);

    /**
     * Gets status for activities in course.
     *
     * @param courseId       the course id
     * @param personTotaraId the person totara id
     * @return the status for activities in course
     */
    List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInCourse(Long courseId, Long personTotaraId);

    /**
     * Gets status for activities in same course.
     *
     * @param activityId     the activity id
     * @param personTotaraId the person totara id
     * @return the status for activities in same course
     */
    List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInSameCourse(Long activityId,
            Long personTotaraId);

    /**
     * Submit feedback activity totara activity completion dto.
     *
     * @param courseId       the course id
     * @param moduleId       the module id
     * @param personTotaraId the person totara id
     * @param data           the data
     * @return the totara activity completion dto
     */
    TotaraActivityCompletionDTO submitFeedbackActivity(Long courseId, Long moduleId, Long personTotaraId, String data);

    /**
     * Submit quiz questions map.
     *
     * @param courseId       the course id
     * @param moudeId        the moude id
     * @param personTotaraId the person totara id
     * @param dto            the dto
     * @return the map
     */
    Map<String, Object> submitQuizQuestions(Long courseId, Long moudeId, Long personTotaraId,
            CoursePlayerActivityQuizSubmitDTO dto);

    /**
     * Save course complete verification.
     *
     * @param courseId       the course id
     * @param personTotaraId the person totara id
     */
    void saveCourseCompleteVerification(Long courseId, Long personTotaraId);

    /**
     * Gets choice.
     *
     * @param personTotaraId the person totara id
     * @param activityId     the activity id
     * @return the choice
     */
    CoursePlayerActivityChoiceDTO getChoice(Long personTotaraId, Long activityId);

    /**
     * Submit choice course player activity choice dto.
     *
     * @param activityTrackingId the activity tracking id
     * @param personTotaraId     the person totara id
     * @param incomingDTO        the incoming dto
     * @return the course player activity choice dto
     */
    CoursePlayerActivityChoiceDTO submitChoice(Long activityTrackingId, Long personTotaraId,
            CoursePlayerActivityChoiceDTO incomingDTO);

    /**
     * Save video time.
     *
     * @param personId       the person id
     * @param personTotaraId the person totara id
     * @param courseId       the course id
     * @param activityId     the activity id
     * @param videoTime      the video time
     */
    void saveVideoTime(Long personId, Long personTotaraId, Long courseId, Long activityId, Double videoTime);

}
