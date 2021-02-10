package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraProgramDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraTeamCourseDTO;
import com.redhat.uxl.dataobjects.domain.dto.TotaraUserWithCountsDTO;
import java.util.List;
import java.util.Set;

/**
 * The interface Totara team dao.
 */
public interface TotaraTeamDAO {

    /**
     * Is manager boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    Boolean isManager(Long userId);

    /**
     * Find team members with counts by manager list.
     *
     * @param managerId the manager id
     * @return the list
     */
    List<TotaraUserWithCountsDTO> findTeamMembersWithCountsByManager(Long managerId);

    /**
     * Find team members programs list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraProgramDTO> findTeamMembersPrograms(Set<Long> userId);

    /**
     * Find team members courses list.
     *
     * @param userId the user id
     * @return the list
     */
    List<TotaraTeamCourseDTO> findTeamMembersCourses(Set<Long> userId);
}
