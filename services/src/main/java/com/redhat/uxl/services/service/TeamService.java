package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.dto.CourseSetProgressionOverviewDTO;
import com.redhat.uxl.services.service.dto.TeamMemberDTO;
import com.redhat.uxl.services.service.dto.TeamMemberProgressOverviewDTO;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Team service.
 */
public interface TeamService {

    /**
     * Is manager boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    @Timed
    @Transactional(readOnly = true)
    Boolean isManager(Long userId);

    /**
     * Find team members by manager list.
     *
     * @param managerId the manager id
     * @return the list
     */
    List<TeamMemberDTO> findTeamMembersByManager(Long managerId);

    /**
     * Find completions by user list.
     *
     * @param teamMembers the team members
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<TeamMemberProgressOverviewDTO> findCompletionsByUser(List<TeamMemberDTO> teamMembers);

    /**
     * Find programs completions by user list.
     *
     * @param teamMembers the team members
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<TeamMemberProgressOverviewDTO> findProgramsCompletionsByUser(List<TeamMemberDTO> teamMembers);

    /**
     * Find shared personal programs completion by user list.
     *
     * @param userId       the user id
     * @param programId    the program id
     * @param teamMemberId the team member id
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<CourseSetProgressionOverviewDTO> findSharedPersonalProgramsCompletionByUser(Long userId, Long programId,
            Long teamMemberId);

    /**
     * Find shared personal programs completions by user list.
     *
     * @param userId      the user id
     * @param teamMembers the team members
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<TeamMemberProgressOverviewDTO> findSharedPersonalProgramsCompletionsByUser(Long userId,
            List<TeamMemberDTO> teamMembers);

    /**
     * Find course completions by user list.
     *
     * @param teamMembers the team members
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<TeamMemberProgressOverviewDTO> findCourseCompletionsByUser(List<TeamMemberDTO> teamMembers);
}
