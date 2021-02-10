package com.redhat.uxl.services.service;

import com.redhat.uxl.datalayer.dto.TotaraProgramDTO;

/**
 * The interface Program player service.
 */
public interface ProgramPlayerService {

    /**
     * Gets program content for user.
     *
     * @param programId      the program id
     * @param personTotaraId the person totara id
     * @return the program content for user
     */
    TotaraProgramDTO getProgramContentForUser(Long programId, Long personTotaraId);
}
