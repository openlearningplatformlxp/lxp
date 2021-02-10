package com.redhat.uxl.services.service;

import com.codahale.metrics.annotation.Timed;
import com.redhat.uxl.dataobjects.domain.PersonalPlanShare;
import com.redhat.uxl.dataobjects.domain.dto.PersonalProgramDTO;
import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Personal plan share service.
 */
public interface PersonalPlanShareService {

    /**
     * Find shared with me plans list.
     *
     * @param userId the user id
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<PersonalProgramDTO> findSharedWithMePlans(Long userId);

    /**
     * Share with manager.
     *
     * @param userId         the user id
     * @param personalPlanId the personal plan id
     * @param managerId      the manager id
     * @param sharedMessage  the shared message
     */
    @Timed
    @Transactional
    void shareWithManager(Long userId, Long personalPlanId, Long managerId, String sharedMessage);

    /**
     * Share with direct reports.
     *
     * @param userId         the user id
     * @param personalPlanId the personal plan id
     * @param directReports  the direct reports
     * @param sharedMessage  the shared message
     * @param dueDate        the due date
     */
    @Timed
    @Transactional
    void shareWithDirectReports(Long userId, Long personalPlanId, List<Long> directReports, String sharedMessage,
            Date dueDate);

    /**
     * Find shares with direct reports list.
     *
     * @param planId the plan id
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<PersonalPlanShare> findSharesWithDirectReports(Long planId);

    /**
     * Find shares with direct reports list.
     *
     * @param planIds the plan ids
     * @return the list
     */
    @Timed
    @Transactional(readOnly = true)
    List<PersonalPlanShare> findSharesWithDirectReports(List<Long> planIds);
}
