package com.redhat.uxl.datalayer.dao;

import com.redhat.uxl.dataobjects.domain.dto.TotaraUserDTO;
import java.util.List;
import java.util.Set;

/**
 * The interface Totara user dao.
 */
public interface TotaraUserDAO {
    /**
     * Find all active users list.
     *
     * @return the list
     */
    List<TotaraUserDTO> findAllActiveUsers();

    /**
     * Find all active users in program list.
     *
     * @param programId the program id
     * @return the list
     */
    List<TotaraUserDTO> findAllActiveUsersInProgram(Long programId);

    /**
     * Find users by ids list.
     *
     * @param userIdList the user id list
     * @return the list
     */
    List<TotaraUserDTO> findUsersByIds(Set<Long> userIdList);

    /**
     * Find user by id totara user dto.
     *
     * @param userId the user id
     * @return the totara user dto
     */
    TotaraUserDTO findUserById(Long userId);
}
