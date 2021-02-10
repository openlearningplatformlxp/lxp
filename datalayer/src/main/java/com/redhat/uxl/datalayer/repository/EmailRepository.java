package com.redhat.uxl.datalayer.repository;

import com.redhat.uxl.dataobjects.domain.Email;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * The interface Email repository.
 */
public interface EmailRepository extends BaseJpaRepository<Email, Long>, JpaSpecificationExecutor<Email> {
    /**
     * Find top by sent date is null and attempt count less than order by created date asc email.
     *
     * @param retryCount the retry count
     * @return the email
     */
    Email findTopBySentDateIsNullAndAttemptCountLessThanOrderByCreatedDateAsc(int retryCount);

    /**
     * Find top by sent date is null and attempt count less than and id not in order by created date asc email.
     *
     * @param retryCount  the retry count
     * @param idsToIgnore the ids to ignore
     * @return the email
     */
    Email findTopBySentDateIsNullAndAttemptCountLessThanAndIdNotInOrderByCreatedDateAsc(int retryCount,
            Set<Long> idsToIgnore);

    /**
     * Gets failed emails.
     *
     * @return the failed emails
     */
    @Query("select e from Email e where e.sentDate is null and e.attemptCount > 0")
    List<Email> getFailedEmails();
}
