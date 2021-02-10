package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathDTO;
import com.redhat.uxl.dataobjects.domain.dto.ProgramCourseSetDTO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import java.util.List;

/**
 * The interface Personal plans dao.
 */
public interface PersonalPlansDAO {

    /**
     * Gets plans for user.
     *
     * @param userId the user id
     * @return list of Personal Programs for user
     */
    List<PersonalProgramDTO> getPlansForUser(Long userId);

    /**
     * Gets plans with due date passed.
     *
     * @return list of Personal Programs with due date passed
     */
    List<PersonalProgramDTO> getPlansWithDueDatePassed();

    /**
     * Gets archived plans for user.
     *
     * @param userId the user id
     * @return List of archived Personal Programs for user
     */
    List<PersonalProgramDTO> getArchivedPlansForUser(Long userId);

    /**
     * Count plans for user integer.
     *
     * @param userId the user id
     * @return the number of plans
     */
    Integer countPlansForUser(Long userId);

    /**
     * Insert plan for user long.
     *
     * @param userId                  the user id
     * @param personalLearningPathDTO the personal learning path dto
     * @return the long
     */
    Long insertPlanForUser(Long userId, PersonalLearningPathDTO personalLearningPathDTO);

    /**
     * Gets personal plan for user.
     *
     * @param userId the user id
     * @param pathId the path id
     * @return the personal plan for user
     */
    PersonalLearningPathDTO getPersonalPlanForUser(Long userId, Long pathId);

    /**
     * Gets course sets for personal plan.
     *
     * @param pathId the path id
     * @return the course sets for personal plan
     */
    List<ProgramCourseSetDTO> getCourseSetsForPersonalPlan(Long pathId);

    /**
     * Update plan for user.
     *
     * @param userId                  the user id
     * @param personalLearningPathDTO the personal learning path dto
     */
    void updatePlanForUser(Long userId, PersonalLearningPathDTO personalLearningPathDTO);

    /**
     * Sets personal plan manual completion.
     *
     * @param itemId the item id
     * @param status the status
     * @param userId the user id
     */
    void setPersonalPlanManualCompletion(Long itemId, Long status, Long userId);

    /**
     * Gets personal plan manual completion.
     *
     * @param itemId the item id
     * @param userId the user id
     * @return the personal plan manual completion
     */
    Long getPersonalPlanManualCompletion(Long itemId, Long userId);

    /**
     * Find plan section course id long.
     *
     * @param userId    the user id
     * @param programId the program id
     * @param sectionId the section id
     * @param courseId  the course id
     * @return the plan section course id
     */
    Long findPlanSectionCourseId(Long userId, Long programId, Long sectionId, Long courseId);

    /**
     * Gets plans shared with user.
     *
     * @param userId the user id
     * @return the list of personal program plans shared with user
     */
    List<PersonalProgramDTO> getPlansSharedWithUser(Long userId);

    /**
     * Gets personal shared plans for user.
     *
     * @param userId the user id
     * @return the personal shared plans for user
     */
    List<PersonalProgramDTO> getPersonalSharedPlansForUser(Long userId);

    /**
     * Archive plan for user.
     *
     * @param userId the user id
     * @param pathId the path id
     */
    void archivePlanForUser(Long userId, Long pathId);
}
