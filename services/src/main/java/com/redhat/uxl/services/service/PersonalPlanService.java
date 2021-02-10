package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.dto.PersonalLearningPathDTO;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import com.redhat.uxl.services.service.dto.LearningPathProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.PersonalProgramStatsDTO;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The interface Personal plan service.
 */
public interface PersonalPlanService {

    /**
     * Gets personal plan stats.
     *
     * @param currentUserId the current user id
     * @param teamMembers   the team members
     * @return the personal plan stats
     */
    @Timed
    @Transactional(readOnly = true)
    PersonalProgramStatsDTO getPersonalPlanStats(Long currentUserId, List<TeamMemberDTO> teamMembers);

    /**
     * Gets personal plans for user.
     *
     * @param userId the user id
     * @return the personal plans for user
     */
    @Timed
    @Transactional(readOnly = true)
    List<PersonalProgramDTO> getPersonalPlansForUser(Long userId);

    /**
     * Gets personal plan for user.
     *
     * @param userId the user id
     * @param pathId the path id
     * @return the personal plan for user
     */
    @Timed
    @Transactional(readOnly = true)
    PersonalLearningPathDTO getPersonalPlanForUser(Long userId, Long pathId);

    /**
     * Gets archived personal plans for user.
     *
     * @param userId the user id
     * @return the archived personal plans for user
     */
    @Timed
    @Transactional(readOnly = true)
    List<PersonalProgramDTO> getArchivedPersonalPlansForUser(Long userId);

    /**
     * Gets personal shared plans for user.
     *
     * @param userId the user id
     * @return the personal shared plans for user
     */
    @Timed
    @Transactional(readOnly = true)
    List<PersonalProgramDTO> getPersonalSharedPlansForUser(Long userId);

    /**
     * Insert personal plan long.
     *
     * @param userId the user id
     * @param path   the path
     * @return the long
     */
    Long insertPersonalPlan(Long userId, PersonalLearningPathDTO path);

    /**
     * Update personal plan.
     *
     * @param userId the user id
     * @param path   the path
     */
    void updatePersonalPlan(Long userId, PersonalLearningPathDTO path);

    /**
     * Sets personal plan manual completion.
     *
     * @param itemId the item id
     * @param status the status
     * @param userId the user id
     */
    void setPersonalPlanManualCompletion(Long itemId, Long status, Long userId);

    /**
     * Gets program statistics.
     *
     * @param programId the program id
     * @param userId    the user id
     * @return the program statistics
     */
    @Timed
    @Transactional(readOnly = true)
    LearningPathProgressionOverviewDTO getProgramStatistics(Long programId, Long userId);

    /**
     * Archive personal program.
     *
     * @param userId the user id
     * @param pathId the path id
     */
    @Timed
    @Transactional(readOnly = true)
    void archivePersonalProgram(Long userId, Long pathId);

    /**
     * Clone personal program long.
     *
     * @param personTotaraId the person totara id
     * @param pathId         the path id
     * @return the long
     */
    @Timed
    @Transactional()
    Long clonePersonalProgram(Long personTotaraId, Long pathId);
}
