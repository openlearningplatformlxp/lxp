package com.redhat.uxl.services.service;

import com.redhat.uxl.dataobjects.domain.LearningLockerJobExecution;
import com.redhat.uxl.dataobjects.domain.LearningLockerJobExecutionDetail;
import com.redhat.uxl.dataobjects.domain.types.LearningLockerJobExecutionType;
import com.redhat.uxl.services.service.dto.LearningLockerResponseDTO;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Learning locker service.
 */
public interface LearningLockerService {

    /**
     * Find last job execution date time.
     *
     * @param type the type
     * @return the date time
     */
    @Transactional(readOnly = true)
    DateTime findLastJobExecution(LearningLockerJobExecutionType type);

    /**
     * Pull statements data after learning locker response dto.
     *
     * @param type        the type
     * @param createdDate the created date
     * @param cursor      the cursor
     * @return the learning locker response dto
     */
    @Transactional
    LearningLockerResponseDTO pullStatementsDataAfter(LearningLockerJobExecutionType type, DateTime createdDate,
            String cursor);

    /**
     * Complete activity.
     *
     * @param type        the type
     * @param email       the email
     * @param externalUrl the external url
     * @param detail      the detail
     */
    @Transactional
    void completeActivity(LearningLockerJobExecutionType type, String email, String externalUrl,
            LearningLockerJobExecutionDetail detail);

    /**
     * Post job execution.
     *
     * @param execution the execution
     * @param details   the details
     */
    @Transactional
    void postJobExecution(LearningLockerJobExecution execution, List<LearningLockerJobExecutionDetail> details);

}
