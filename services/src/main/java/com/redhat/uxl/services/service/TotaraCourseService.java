package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseSetDTO;
import com.redhat.uxl.dataobjects.domain.CourseDocument;
import com.redhat.uxl.dataobjects.domain.dto.TotaraCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraEventDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.datalayer.dto.CoursePlayerCourseDTO;
import com.redhat.uxl.services.service.dto.CourseUpcomingClassDTO;
import com.redhat.uxl.services.service.dto.LearningPathDTO;
import com.redhat.uxl.services.service.dto.TotaraLeaningPathDTO;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * The interface Totara course service.
 */
public interface TotaraCourseService {

    /**
     * Find active courses list.
     *
     * @return the list
     */
    List<com.redhat.uxl.services.service.dto.TotaraCourseDTO> findActiveCourses();

    /**
     * Find active courses page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    Page<TotaraCourseDTO> findActiveCourses(int page, int size);

    /**
     * Find upcoming classes list.
     *
     * @param courseId       the course id
     * @param personTotaraId the person totara id
     * @return the list
     */
    List<CourseUpcomingClassDTO> findUpcomingClasses(Long courseId, Long personTotaraId);

    /**
     * Find by program id and user id learning path dto.
     *
     * @param programId the program id
     * @param userId    the user id
     * @return the learning path dto
     */
    LearningPathDTO findByProgramIdAndUserId(Long programId, Long userId);

    /**
     * Find active programs list.
     *
     * @return the list
     */
    List<TotaraLeaningPathDTO> findActivePrograms();

    /**
     * Gets enrolled course.
     *
     * @param personTotaraId the person totara id
     * @param courseTotaraId the course totara id
     * @return the enrolled course
     */
    CoursePlayerCourseDTO getEnrolledCourse(Long personTotaraId, Long courseTotaraId);

    /**
     * Gets program course sets.
     *
     * @param programId      the program id
     * @param personTotaraId the person totara id
     * @return the program course sets
     */
    List<ProgramCourseSetDTO> getProgramCourseSets(Long programId, Long personTotaraId);

    /**
     * Build course documents list.
     *
     * @param content the content
     * @return the list
     */
    List<CourseDocument> buildCourseDocuments(List<TotaraCourseDTO> content);

    /**
     * Build course documents from program list.
     *
     * @param content the content
     * @return the list
     */
    List<CourseDocument> buildCourseDocumentsFromProgram(List<TotaraProgramDTO> content);

    /**
     * Build course documents from event list.
     *
     * @param events the events
     * @return the list
     */
    List<CourseDocument> buildCourseDocumentsFromEvent(List<TotaraEventDTO> events);

    /**
     * Find active programs page.
     *
     * @param page           the page
     * @param defaultMaxSize the default max size
     * @return the page
     */
    Page<TotaraProgramDTO> findActivePrograms(int page, int defaultMaxSize);

    /**
     * Find active events page.
     *
     * @param page           the page
     * @param defaultMaxSize the default max size
     * @return the page
     */
    Page<TotaraEventDTO> findActiveEvents(int page, int defaultMaxSize);

    /**
     * Update text entry result.
     *
     * @param userId      the user id
     * @param activityId  the activity id
     * @param textEntryId the text entry id
     * @param message     the message
     */
    void updateTextEntryResult(Long userId, Long activityId, Long textEntryId, String message);

}
