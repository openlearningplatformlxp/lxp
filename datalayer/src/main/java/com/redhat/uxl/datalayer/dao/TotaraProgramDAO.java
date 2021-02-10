package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.datalayer.dto.LearningPathProgressionDTO;
import com.redhat.uxl.datalayer.dto.TotaraCourseSetDTO;
import com.redhat.uxl.datalayer.dto.TotaraProgramDTO;
import com.redhat.uxl.datalayer.dto.TotaraTextActivityDTO;

import java.util.List;

/**
 * The interface Totara program dao.
 */
public interface TotaraProgramDAO {
    /**
     * Gets program by id.
     *
     * @param programId the program id
     * @return the program by id
     */
    TotaraProgramDTO getProgramById(Long programId);

    /**
     * Gets course sets in program.
     *
     * @param personTotaraId the person totara id
     * @param programId      the program id
     * @param withStatus     the with status
     * @return the course sets in program
     */
    List<TotaraCourseSetDTO> getCourseSetsInProgram(Long personTotaraId, Long programId, Boolean withStatus);

    /**
     * Is user enrolled in program boolean.
     *
     * @param personTotaraId the person totara id
     * @param programId      the program id
     * @return the boolean
     */
    Boolean isUserEnrolledInProgram(Long personTotaraId, Long programId);

    /**
     * Find learning path progression list.
     *
     * @param programId the program id
     * @param userId    the user id
     * @return the list
     */
    List<LearningPathProgressionDTO> findLearningPathProgression(Long programId, Long userId);

    /**
     * Find learning path total duration learning path progression dto.
     *
     * @param programId the program id
     * @return the learning path progression dto
     */
    LearningPathProgressionDTO findLearningPathTotalDuration(Long programId);

    /**
     * Is course a nested program long.
     *
     * @param courseId the course id
     * @return the long
     */
    Long isCourseANestedProgram(Long courseId);

    /**
     * Is course a text entry totara text activity dto.
     *
     * @param userId   the user id
     * @param courseId the course id
     * @return the totara text activity dto
     */
    TotaraTextActivityDTO isCourseATextEntry(Long userId, Long courseId);

    /**
     * Find learning path progression non enrolled list.
     *
     * @param programId the program id
     * @return the list
     */
    List<LearningPathProgressionDTO> findLearningPathProgressionNonEnrolled(Long programId);

    /**
     * Drop user.
     *
     * @param programId the program id
     * @param userId    the user id
     */
    void dropUser(Long programId, Long userId);
}
