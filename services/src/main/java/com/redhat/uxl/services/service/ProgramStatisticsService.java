package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.services.service.dto.LearningPathProgressionOverviewDTO;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Program statistics service.
 */
public interface ProgramStatisticsService {

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

}
