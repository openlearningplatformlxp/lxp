package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.datalayer.dto.CoursePlayerActivityChoiceDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityQuizSubmitDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerActivityStatusAvailabilityDTO;
import com.redhat.uxl.datalayer.dto.QuizScoreDTO;
import com.redhat.uxl.datalayer.dto.TotaraActivityCompletionDTO;

import java.util.List;

/**
 * The interface Totara activity dao.
 */
public interface TotaraActivityDAO {

    /**
     * Gets activity count for program.
     *
     * @param programId the program id
     * @return the activity count for program
     */
    Long getActivityCountForProgram(Long programId);

    /**
     * Gets activity count.
     *
     * @param courseId the course id
     * @return the activity count
     */
    public Long getActivityCount(Long courseId);

    /**
     * Gets completed activity count.
     *
     * @param courseId the course id
     * @param userId   the user id
     * @return the completed activity count
     */
    public Long getCompletedActivityCount(Long courseId, Long userId);

    /**
     * Gets course status.
     *
     * @param courseId the course id
     * @param userId   the user id
     * @return the course status
     */
    public Long getCourseStatus(Long courseId, Long userId);

    /**
     * Complete url single act course.
     *
     * @param courseId the course id
     * @param userId   the user id
     */
    public void completeURLSingleActCourse(Long courseId, Long userId);

    /**
     * Gets activities by section.
     *
     * @param courseId  the course id
     * @param sectionId the section id
     * @return the activities by section
     */
    public List<CoursePlayerActivityDTO> getActivitiesBySection(Long courseId, Long sectionId); //To figure out

    /**
     * Gets activity content.
     *
     * @param activityId     the activity id
     * @param personId       the person id
     * @param personTotaraId the person totara id
     * @return the activity content
     */
    public CoursePlayerActivityDTO getActivityContent(Long activityId, Long personId, Long personTotaraId);

    /**
     * Gets status for activities in course.
     *
     * @param courseId       the course id
     * @param personTotaraId the person totara id
     * @return the status for activities in course
     */
    public List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInCourse(Long courseId, Long personTotaraId);

    /**
     * Gets status for activities in same course.
     *
     * @param activityId     the activity id
     * @param personTotaraId the person totara id
     * @return the status for activities in same course
     */
    public List<CoursePlayerActivityStatusAvailabilityDTO> getStatusForActivitiesInSameCourse(Long activityId, Long personTotaraId);

    /**
     * Complete activity totara activity completion dto.
     *
     * @param moduleId       the module id
     * @param personTotaraId the person totara id
     * @param targetState    the target state
     * @return the totara activity completion dto
     */
    public TotaraActivityCompletionDTO completeActivity(Long moduleId, Long personTotaraId, int targetState);

    /**
     * Complete activity with data totara activity completion dto.
     *
     * @param moduleId       the module id
     * @param personTotaraId the person totara id
     * @param targetState    the target state
     * @param data           the data
     * @return the totara activity completion dto
     */
    public TotaraActivityCompletionDTO completeActivityWithData(Long moduleId, Long personTotaraId, int targetState, String data);

    /**
     * Submit totara activity quiz question answer string.
     *
     * @param dto            the dto
     * @param personTotaraId the person totara id
     * @return the string
     */
    public String submitTotaraActivityQuizQuestionAnswer(CoursePlayerActivityQuizSubmitDTO dto, Long personTotaraId);

    /**
     * Submit totara activity quiz attempt quiz score dto.
     *
     * @param attemptid      the attemptid
     * @param personTotaraId the person totara id
     * @return the quiz score dto
     */
    public QuizScoreDTO submitTotaraActivityQuizAttempt(Long attemptid, Long personTotaraId);

    /**
     * Gets choice content.
     *
     * @param personTotaraId the person totara id
     * @param activityId     the activity id
     * @return the choice content
     */
    public CoursePlayerActivityChoiceDTO getChoiceContent(Long personTotaraId, Long activityId);

    /**
     * Submit choice course player activity choice dto.
     *
     * @param totaraId the totara id
     * @param moduleId the module id
     * @param postData the post data
     * @return the course player activity choice dto
     */
    public CoursePlayerActivityChoiceDTO submitChoice(Long totaraId, Long moduleId, CoursePlayerActivityChoiceDTO postData);

}
