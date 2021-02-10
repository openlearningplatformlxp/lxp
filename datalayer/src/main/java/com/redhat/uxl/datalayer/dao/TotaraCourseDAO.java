package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.datalayer.dto.CoursePlayerCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraActivityDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraActivityWrapperDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraFaceToFaceSessionDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraHtmlBlockDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.types.CourseDocumentVisibilityType;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Totara course dao.
 */
public interface TotaraCourseDAO {

    /**
     * Find active courses list.
     *
     * @return the list
     */
    List<TotaraCourseDTO> findActiveCourses();

    /**
     * Gets enrolled course.
     *
     * @param personTotaraId the person totara id
     * @param courseTotaraId the course totara id
     * @return the enrolled course
     */
    CoursePlayerCourseDTO getEnrolledCourse(Long personTotaraId, Long courseTotaraId);

    /**
     * Gets ce credits for user.
     *
     * @param userId the user id
     * @return the ce credits for user
     */
    int getCECreditsForUser(Long userId);

    /**
     * Find by user id and program id list.
     *
     * @param userId    the user id
     * @param programId the program id
     * @return the list
     */
    List<TotaraProgramDTO> findByUserIdAndProgramId(Long userId, Long programId);

    /**
     * Find programs by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraProgramDTO> findProgramsByUserId(Long userId);

    /**
     * Find in progress programs by user id page.
     *
     * @param userId   the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<TotaraProgramDTO> findInProgressProgramsByUserId(Long userId, Pageable pageable);

    /**
     * Find courses by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraCourseDTO> findCoursesByUserId(Long userId);

    /**
     * Find in progress courses by user id page.
     *
     * @param userId   the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<TotaraCourseDTO> findInProgressCoursesByUserId(Long userId, Pageable pageable);

    /**
     * Find activity by instance id totara activity dto.
     *
     * @param instanceId the instance id
     * @return the totara activity dto
     */
    TotaraActivityDTO findActivityByInstanceId(Long instanceId);

    /**
     * Find activities by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraActivityDTO> findActivitiesByUserId(Long userId);

    /**
     * Find allowed user audience by user id long.
     *
     * @param userId    the user id
     * @param programId the program id
     * @return the long
     */
    Long findAllowedUserAudienceByUserId(Long userId, Long programId);

    /**
     * Find active programs list.
     *
     * @return the list
     */
    List<TotaraProgramDTO> findActivePrograms();

    /**
     * Find active programs page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<TotaraProgramDTO> findActivePrograms(Pageable pageable);

    /**
     * Find active programs by id page.
     *
     * @param programIdList the program id list
     * @param pageable      the pageable
     * @return the page
     */
    Page<TotaraProgramDTO> findActiveProgramsById(List<Long> programIdList, Pageable pageable);

    /**
     * Find active courses page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<TotaraCourseDTO> findActiveCourses(Pageable pageable);

    /**
     * Find active courses by tag page.
     *
     * @param tagId    the tag id
     * @param pageable the pageable
     * @return the page
     */
    Page<TotaraCourseDTO> findActiveCoursesByTag(Long tagId, Pageable pageable);

    /**
     * Find completed activities by user id list.
     *
     * @param userId the user id
     * @param max    the max
     * @return the list
     */
    List<TotaraActivityDTO> findCompletedActivitiesByUserId(Long userId, int max);

    /**
     * Find activity for user courses list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraActivityDTO> findActivityForUserCourses(Long userId);

    /**
     * Find active courses by audience page.
     *
     * @param audienceId the audience id
     * @param pageable   the pageable
     * @return the page
     */
    Page<TotaraCourseDTO> findActiveCoursesByAudience(Long audienceId, Pageable pageable);

    /**
     * Inert activity text.
     *
     * @param activityId the activity id
     * @param userId     the user id
     * @param message    the message
     */
    void inertActivityText(Long activityId, Long userId, String message);

    /**
     * Find activity id long.
     *
     * @param activityId the activity id
     * @param course     the course
     * @return the long
     */
    Long findActivityId(Long activityId, Long course);

    /**
     * Complete totara course activity db.
     *
     * @param activityId the activity id
     * @param userId     the user id
     */
    void completeTotaraCourseActivityDB(Long activityId, Long userId);

    /**
     * Find face to face sessions list.
     *
     * @param courseId the course id
     * @return the list
     */
    List<TotaraFaceToFaceSessionDTO> findFaceToFaceSessions(Long courseId);

    /**
     * Find face to face session by id totara face to face session dto.
     *
     * @param sessionId the session id
     * @return the totara face to face session dto
     */
    TotaraFaceToFaceSessionDTO findFaceToFaceSessionById(Long sessionId);

    /**
     * Find active programs by tag page.
     *
     * @param tagId       the tag id
     * @param pageRequest the page request
     * @return the page
     */
    Page<TotaraProgramDTO> findActiveProgramsByTag(Long tagId, Pageable pageRequest);

    /**
     * Find program by course id totara program dto.
     *
     * @param courseId the course id
     * @return the totara program dto
     */
    TotaraProgramDTO findProgramByCourseId(Long courseId);

    /**
     * Find program by program id totara program dto.
     *
     * @param programId the program id
     * @return the totara program dto
     */
    TotaraProgramDTO findProgramByProgramId(Long programId);

    /**
     * Find program html block by program id totara html block dto.
     *
     * @param programId the program id
     * @return the totara html block dto
     */
    TotaraHtmlBlockDTO findProgramHtmlBlockByProgramId(Long programId);

    /**
     * Find course by course id totara course dto.
     *
     * @param courseId the course id
     * @return the totara course dto
     */
    TotaraCourseDTO findCourseByCourseId(Long courseId);

    /**
     * Add face to face signup int.
     *
     * @param sessionId the session id
     * @param userId    the user id
     * @return the int
     */
    int addFaceToFaceSignup(Long sessionId, Long userId);

    /**
     * Add face to face signup status int.
     *
     * @param signupId the signup id
     * @return the int
     */
    int addFaceToFaceSignupStatus(Long signupId);

    /**
     * Enroll in course int.
     *
     * @param courseId the course id
     * @param userId   the user id
     * @return the int
     */
    int enrollInCourse(Long courseId, Long userId);

    /**
     * Find program enrollments list.
     *
     * @param fromDateTime the from date time
     * @return the list
     */
    List<TotaraProgramDTO> findProgramEnrollments(DateTime fromDateTime);

    /**
     * Find overdue program enrollments list.
     *
     * @param fromDateTime the from date time
     * @return the list
     */
    List<TotaraProgramDTO> findOverdueProgramEnrollments(DateTime fromDateTime);

    /**
     * Find completed programs by user list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraProgramDTO> findCompletedProgramsByUser(Long userId);

    /**
     * Find completed courses by user list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraCourseDTO> findCompletedCoursesByUser(Long userId);

    /**
     * Find courses by id list.
     *
     * @param courseIdList the course id list
     * @return the list
     */
    List<TotaraCourseDTO> findCoursesById(List<Long> courseIdList);

    /**
     * Visibility type for course course document visibility type.
     *
     * @param courseId the course id
     * @return the course document visibility type
     */
    CourseDocumentVisibilityType visibilityTypeForCourse(Long courseId);

    /**
     * Visibility type for learning path course document visibility type.
     *
     * @param programId the program id
     * @return the course document visibility type
     */
    CourseDocumentVisibilityType visibilityTypeForLearningPath(Long programId);

    /**
     * Enrolled in for program long.
     *
     * @param userId    the user id
     * @param programId the program id
     * @return the long
     */
    Long enrolledInForProgram(Long userId, Long programId);

    /**
     * Find allowed user audience for course list.
     *
     * @param courseId the course id
     * @return the list
     */
    List<Long> findAllowedUserAudienceForCourse(Long courseId);

    /**
     * Find allowed user audience for program list.
     *
     * @param programId the program id
     * @return the list
     */
    List<Long> findAllowedUserAudienceForProgram(Long programId);
}
